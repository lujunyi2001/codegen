package org.sonic.codegen.gen;

import org.apache.commons.lang.StringUtils;
import org.sonic.codegen.dto.GeneratorParam;
import org.sonic.codegen.entity.TableDefinition;
import org.sonic.codegen.util.FieldUtil;

/**
 * SQL上下文,这里可以取到表,字段信息<br>
 * 最终会把SQL上下文信息放到velocity中
 */
public class SQLContext {

    /**
     * 表结构定义
     */
    private final TableDefinition tableDefinition;
    private final GeneratorParam generatorParam;
    private JavaColumnDefinition javaPkColumn;
    /**
     * 包名
     */
    private String packageName;
    /**
     * 包名前缀
     */
    private String packageNamePrefix;

    /**
     * 删除的前缀
     */
    private String delPrefix;

    /**
     * 数据库名
     */
    private String dbName;

    public SQLContext(TableDefinition tableDefinition, GeneratorParam generatorParam) {
        this.tableDefinition = tableDefinition;
        this.generatorParam = generatorParam;
        this.delPrefix = this.generatorParam.getDelPrefix();
        // 默认为全字母小写的类名
        this.packageName = getJavaBeanName().toLowerCase();
        if(StringUtils.isNotBlank(tableDefinition.getPackageName())){
            this.packageName = tableDefinition.getPackageName();
        }
//        if(tableDefinition.getPackageName() == null){
//            tableDefinition.setPackageName(packageName);
//        }
        if(tableDefinition.getClassName() == null){
            tableDefinition.setClassName(this.getClassName());
        }
        if(tableDefinition.getModuleName() == null){
            tableDefinition.setModuleName(this.packageName);
        }

        if(this.tableDefinition.getPkColumn() != null && this.tableDefinition.getPkColumn() instanceof JavaColumnDefinition){
            this.javaPkColumn = (JavaColumnDefinition) this.tableDefinition.getPkColumn();
        }
        this.packageNamePrefix = generatorParam.getPackageNamePrefix();
    }

    /**
     * 返回Java类名
     *
     * @return
     */
    public String getJavaBeanName() {
        return getClassName();
    }

    /**
     * 返回类名
     * @return
     */
    public String getClassName() {
        if(!StringUtils.isEmpty(tableDefinition.getClassName())){
            return tableDefinition.getClassName();
        }
        String tableName = getJavaBeanNameLF();
        return FieldUtil.upperFirstLetter(tableName);
    }

    /**
     * 返回Java类名且首字母小写
     *
     * @return
     */
    public String getJavaBeanNameLF() {
        if(!StringUtils.isEmpty(tableDefinition.getClassName())){
            return FieldUtil.lowerFirstLetter(tableDefinition.getClassName());
        }
        String tableName = tableDefinition.getTableName();
        if(delPrefix != null){
            String[] split = delPrefix.split(",");
            for (String prefix : split){
                tableName = tableName.startsWith(prefix) && !StringUtils.isEmpty(prefix) ? tableName.replace(prefix, "") : tableName;
            }
        }

        tableName = FieldUtil.underlineFilter(tableName);
        tableName = FieldUtil.dotFilter(tableName);
        return FieldUtil.lowerFirstLetter(tableName);
    }

    public String getPkName() {
        if (javaPkColumn != null) {
            return javaPkColumn.getColumnName();
        }
        return "";
    }

    public String getJavaPkName() {
        if (javaPkColumn != null) {
            return javaPkColumn.getJavaFieldName();
        }
        return "";
    }

    public String getJavaPkType() {
        if (javaPkColumn != null) {
            return javaPkColumn.getJavaType();
        }
        return "";
    }

    public String getMybatisPkType() {
        if (javaPkColumn != null) {
            return javaPkColumn.getMybatisJdbcType();
        }
        return "";
    }

    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDelPrefix() {
        return delPrefix;
    }

    public void setDelPrefix(String delPrefix) {
        this.delPrefix = delPrefix;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getPackageNamePrefix() {
        return this.packageNamePrefix;
    }

    public void setPackageNamePrefix(String packageNamePrefix) {
        this.packageNamePrefix = packageNamePrefix;
    }
}
