package org.sonic.codegen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.sonic.codegen.config.Comment;
import org.sonic.codegen.entity.ColumnDefinition;
import org.sonic.codegen.gen.CsharpColumnDefinition;
import org.sonic.codegen.gen.JavaColumnDefinition;
import org.sonic.codegen.gen.TypeEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 数据库表定义,从这里可以获取表名,字段信息
 */
@Entity
@Table(name = "GEN_TABLE")
@Comment("数据表配置表")
public class TableDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("主键")
    private Long id;

    /**
     * 表名
     */
    @Column(name = "DATASOURCE_CONFIG_ID")
    @Comment("数据源ID")
    @NotNull(message = "数据源id不能为空")
    private Long datasourceConfigId;

    /**
     * 表名
     */
    @Column(name = "TABLE_NAME")
    @Comment("表名")
    private String tableName;

    /**
     * 语言类型
     */
    @Column(name = "LANGUAGE_TYPE")
    @Comment("语言类型")
    private String languageType;

    /**
     * 表注释
     */
    @Column(name = "COMMENT")
    @Comment("表注释")
    private String comment;

    /**
     * 模块名
     */
    @Column(name = "MODULE_NAME")
    @Comment("模块名")
    private String moduleName;

    /**
     * 类名
     */
    @Column(name = "CLASS_NAME")
    @Comment("类名")
    private String className;


    /**
     * 模块名
     */
    @Column(name = "PACKAGE_NAME")
    @Comment("包名")
    private String packageName;

    @Transient
    private boolean generatingCode;

    /** Java相关字段 */
    private transient List<ColumnDefinition> columnDefinitions = Collections.emptyList();

    public TableDefinition() {
    }

    public TableDefinition(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 是否有时间字段
     * @return true：有
     */
    public boolean getHasDateColumn() {
        for (ColumnDefinition definition : columnDefinitions) {
            if (TypeEnum.DATETIME.getType().equalsIgnoreCase(definition.getType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否含有时间字段
     *
     * @return
     */
    public boolean getHasDateField() {
        for (ColumnDefinition definition : columnDefinitions) {
            if (this.generatingCode && definition instanceof JavaColumnDefinition && Date.class.getSimpleName().equals(((JavaColumnDefinition) definition).getJavaType())) {
                return true;
            }
        }
        return false;
    }

    public boolean getHasLocalDateField() {
        for (ColumnDefinition definition : columnDefinitions) {
            if (this.generatingCode && definition instanceof JavaColumnDefinition && LocalDate.class.getSimpleName().equals(((JavaColumnDefinition) definition).getJavaType())) {
                return true;
            }
        }
        return false;
    }

    public boolean getHasLocalDateTimeField() {
        for (ColumnDefinition definition : columnDefinitions) {
            if (this.generatingCode && definition instanceof JavaColumnDefinition && LocalDateTime.class.getSimpleName().equals(((JavaColumnDefinition) definition).getJavaType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否含有BigDecimal字段
     *
     * @return
     */
    public boolean getHasBigDecimalField() {
        for (ColumnDefinition definition : columnDefinitions) {
            if (this.generatingCode && (definition instanceof JavaColumnDefinition) &&
                    "BigDecimal".equals(((JavaColumnDefinition) definition).getJavaType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取主键信息
     *
     * @return 返回主键信息，如果没有则抛出异常
     */
    @JsonIgnore
    public ColumnDefinition getPkColumn() {
        ColumnDefinition pk = null;
        for (ColumnDefinition column : columnDefinitions) {
            if (column.getColumnName().equalsIgnoreCase("id")) {
                pk = column;
            }
            if (column.getIsPk()) {
                return column;
            }
        }
        if (pk != null) {
            return pk;
        }
        throw new RuntimeException(tableName + "表未设置主键");
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ColumnDefinition> getColumnDefinitions() {
        return columnDefinitions;
    }

    public void setColumnDefinitions(List<ColumnDefinition> columnDefinitions) {
        this.columnDefinitions = columnDefinitions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Long getDatasourceConfigId() {
        return datasourceConfigId;
    }

    public void setDatasourceConfigId(Long datasourceConfigId) {
        this.datasourceConfigId = datasourceConfigId;
    }

    public String getLanguageType() {
        return languageType;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isGeneratingCode() {
        return generatingCode;
    }

    public void setGeneratingCode(boolean generatingCode) {
        this.generatingCode = generatingCode;
    }
}
