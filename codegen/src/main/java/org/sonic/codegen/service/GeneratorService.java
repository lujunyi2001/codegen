package org.sonic.codegen.service;

import org.apache.velocity.VelocityContext;
import org.sonic.codegen.dto.GeneratorParam;
import org.sonic.codegen.entity.ColumnDefinition;
import org.sonic.codegen.entity.TemplateConfig;
import org.sonic.codegen.gen.*;
import org.sonic.codegen.entity.TableDefinition;
import org.sonic.codegen.util.VelocityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 生成代码逻辑
 */
@Service
public class GeneratorService {

    @Autowired
    private TemplateConfigService templateConfigService;
    @Autowired
    private TableDefinitionService tableDefinitionService;

    /**
     * 生成代码内容,map的
     *
     * @param generatorParam 生成参数
     * @param generatorConfig 数据源配置
     * @return 一张表对应多个模板
     */
    public List<CodeFile> generate(GeneratorParam generatorParam, GeneratorConfig generatorConfig) {
        List<SQLContext> contextList = this.buildSQLContextList(generatorParam, generatorConfig, true);
        List<CodeFile> codeFileList = new ArrayList<>();

        for (SQLContext sqlContext : contextList) {
            setStringValue(t->sqlContext.setPackageNamePrefix(t), generatorParam.getPackageNamePrefix());
            setDelPrefix(sqlContext, generatorParam.getDelPrefix());
            for (Long tcId : generatorParam.getTemplateConfigIdList()) {
                TemplateConfig template = templateConfigService.getById(tcId);
                String folder = template.getName();
                String fileName = doGenerator(sqlContext, template.getFileName());
                String content = doGenerator(sqlContext, template.getContent());
                CodeFile codeFile = new CodeFile();
                codeFile.setFolder(folder);
                codeFile.setFileName(fileName);
                codeFile.setContent(content);
                codeFileList.add(codeFile);
            }
        }

        return codeFileList;
    }

    public List<CodeFile> generateFromTableDefinition(GeneratorParam generatorParam, GeneratorConfig generatorConfig, TableDefinition tableDefinition) {
        List<CodeFile> codeFileList = new ArrayList<>();
        List<ColumnDefinition> definitions = TableSelector.buildRealColumnDefinitions(tableDefinition.getColumnDefinitions(), JavaColumnDefinition::new);
        tableDefinition.setColumnDefinitions(definitions);

        SQLContext sqlContext = new SQLContext(tableDefinition, generatorParam);
        setStringValue(t->sqlContext.setPackageName(t), tableDefinition.getPackageName());
        setStringValue(t->sqlContext.setPackageNamePrefix(t), generatorParam.getPackageNamePrefix());
        sqlContext.setDelPrefix(null);
        for (Long tcId : generatorParam.getTemplateConfigIdList()) {
            TemplateConfig template = templateConfigService.getById(tcId);
            String folder = template.getName();
            String fileName = doGenerator(sqlContext, template.getFileName());
            String content = doGenerator(sqlContext, template.getContent());
            CodeFile codeFile = new CodeFile();
            codeFile.setFolder(folder);
            codeFile.setFileName(fileName);
            codeFile.setContent(content);
            codeFileList.add(codeFile);
        }
        return codeFileList;
    }

    @Transactional
    public List<TableDefinition> generateTableDefinition(GeneratorParam generatorParam, GeneratorConfig generatorConfig){
        List<SQLContext> contextList = this.buildSQLContextList(generatorParam, generatorConfig, false);
        for (SQLContext sqlContext : contextList) {
            TableDefinition definition = sqlContext.getTableDefinition();
            definition.setDatasourceConfigId(generatorParam.getDatasourceConfigId());
        }

        for (SQLContext sqlContext : contextList) {
            TableDefinition definition = sqlContext.getTableDefinition();
            List<TableDefinition> tableDefinitions = tableDefinitionService.queryByDatasourceConfigIdAndTableName(definition.getDatasourceConfigId(), definition.getTableName());
            for (TableDefinition old : tableDefinitions) {
                tableDefinitionService.delete(old.getId());
            }
            tableDefinitionService.saveOrUpdate(definition);
        }

        return contextList.stream().map(t->t.getTableDefinition()).collect(Collectors.toList());
    }


    /**
     * 返回SQL上下文列表
     *
     * @param generatorParam 参数
     * @param generatorConfig 配置
     * @return 返回SQL上下文
     */
    private List<SQLContext> buildSQLContextList(GeneratorParam generatorParam, GeneratorConfig generatorConfig, boolean isUseColumnDefinition) {

        List<String> tableNames = generatorParam.getTableNames();
        List<SQLContext> contextList = new ArrayList<>();
        SQLService service = SQLServiceFactory.build(generatorConfig);

        TableSelector tableSelector = service.getTableSelector(generatorConfig);
        tableSelector.setSchTableNames(tableNames);

        List<TableDefinition> tableDefinitions = tableSelector.getTableDefinitions(generatorParam, isUseColumnDefinition);

        for (TableDefinition tableDefinition : tableDefinitions) {
            tableDefinition.setDatasourceConfigId(generatorParam.getDatasourceConfigId());
            tableDefinition.setLanguageType("java");
            SQLContext sqlContext = new SQLContext(tableDefinition, generatorParam);
            setStringValue(t->tableDefinition.setPackageName(t), sqlContext.getPackageName());
            sqlContext.setDbName(generatorConfig.getDbName());
            contextList.add(sqlContext);
        }

        return contextList;
    }

    private void setStringValue(Consumer<String> consumer, String packageName) {
        if (StringUtils.hasText(packageName)) {
            consumer.accept(packageName);
        }
    }

    private void setDelPrefix(SQLContext sqlContext, String delPrefix) {
        if (StringUtils.hasText(delPrefix)) {
            sqlContext.setDelPrefix(delPrefix);
        }
    }

    private String doGenerator(SQLContext sqlContext, String template) {
        if (template == null) {
            return "";
        }
        VelocityContext context = new VelocityContext();

        context.put("context", sqlContext);
        context.put("table", sqlContext.getTableDefinition());
        context.put("pk", sqlContext.getTableDefinition().getPkColumn());
        context.put("columns", sqlContext.getTableDefinition().getColumnDefinitions());

        return VelocityUtil.generate(context, template);
    }


}
