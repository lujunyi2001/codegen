package org.sonic.codegen.gen;

import org.sonic.codegen.dto.GeneratorParam;
import org.sonic.codegen.entity.ColumnDefinition;
import org.sonic.codegen.entity.TableDefinition;
import org.sonic.codegen.util.FieldUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class TableSelector {

    private ColumnSelector columnSelector;
    private GeneratorConfig generatorConfig;
    private List<String> schTableNames;

    public TableSelector(ColumnSelector columnSelector, GeneratorConfig generatorConfig) {
        this.generatorConfig = generatorConfig;
        this.columnSelector = columnSelector;
    }

    /**
     * 查询数据库表的SQL
     * 1.如果是oracle的話則應該傳入用戶名，oracle是根據用戶去管理數據的
     * 2.mysql的话是传入数据库名，mysql和sqlserver是根据数据库去管理的
     *
     * @return
     */
    protected abstract String getShowTablesSQL(String showParam);

    protected abstract TableDefinition buildTableDefinition(Map<String, Object> tableMap);

    public List<TableDefinition> getTableDefinitions(GeneratorParam generatorParam, boolean isUseColumnDefinition) {
        String showParam = generatorConfig.getDbName();
        // 如果是oracle数据库则传oracle数据库用户大写
        if (generatorConfig.getDriverClass().contains("oracle")) {
            showParam = generatorConfig.getUsername().toUpperCase();
        }
        List<Map<String, Object>> resultList = SqlHelper.runSql(getGeneratorConfig(), getShowTablesSQL(showParam));
        List<TableDefinition> tablesList = new ArrayList<TableDefinition>(resultList.size());

        for (Map<String, Object> rowMap : resultList) {
            TableDefinition tableDefinition = this.buildTableDefinition(rowMap);
            tableDefinition.setGeneratingCode(isUseColumnDefinition);
            String tableName = tableDefinition.getTableName();
            List<ColumnDefinition> columnDefinitions = columnSelector.getColumnDefinitions(tableName);
            JavaColumnDefinition javaColumnDefinition = new JavaColumnDefinition();
            for (ColumnDefinition columnDefinition : columnDefinitions) {
                columnDefinition.setFieldName(FieldUtil.underlineFilter(columnDefinition.getColumnName()));
                columnDefinition.setFieldType(javaColumnDefinition.getColumnTypeConverter().convertTypeBox(columnDefinition.getType()));
            }
            if(isUseColumnDefinition){
                columnDefinitions = buildRealColumnDefinitions(columnDefinitions, JavaColumnDefinition::new);
            }
            tableDefinition.setColumnDefinitions(columnDefinitions);
            tablesList.add(tableDefinition);
        }

        return tablesList;
    }

    public static <T> List<T> buildRealColumnDefinitions(List<ColumnDefinition> columnDefinitions, Supplier<T> supplier) {
        return columnDefinitions.stream()
                .map(columnDefinition -> {
                    T t = supplier.get();
                    BeanUtils.copyProperties(columnDefinition, t);
                    return t;
                })
                .collect(Collectors.toList());
    }

    public List<TableDefinition> getSimpleTableDefinitions() {
        String showParam = generatorConfig.getDbName();
        // 如果是oracle数据库则传oracle数据库用户大写
        if (generatorConfig.getDriverClass().contains("oracle")) {
            showParam = generatorConfig.getUsername().toUpperCase();
        }
        List<Map<String, Object>> resultList = SqlHelper.runSql(getGeneratorConfig(), getShowTablesSQL(showParam));
        List<TableDefinition> tablesList = new ArrayList<TableDefinition>(resultList.size());

        for (Map<String, Object> rowMap : resultList) {
            tablesList.add(this.buildTableDefinition(rowMap));
        }

        return tablesList;
    }

    public List<String> wrapTableNames() {
        List<String> schTableNames = this.getSchTableNames();
        if (CollectionUtils.isEmpty(schTableNames)) {
            return Collections.emptyList();
        }
        return schTableNames.stream()
                .map(tableName -> String.format("'%s'", tableName))
                .collect(Collectors.toList());
    }

    public GeneratorConfig getGeneratorConfig() {
        return generatorConfig;
    }

    public void setGeneratorConfig(GeneratorConfig generatorConfig) {
        this.generatorConfig = generatorConfig;
    }

    public ColumnSelector getColumnSelector() {
        return columnSelector;
    }

    public void setColumnSelector(ColumnSelector columnSelector) {
        this.columnSelector = columnSelector;
    }

    public List<String> getSchTableNames() {
        return schTableNames;
    }

    public void setSchTableNames(List<String> schTableNames) {
        this.schTableNames = schTableNames;
    }

}
