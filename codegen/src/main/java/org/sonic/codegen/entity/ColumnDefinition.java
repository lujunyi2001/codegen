package org.sonic.codegen.entity;

import org.sonic.codegen.config.Comment;
import org.sonic.codegen.gen.converter.ColumnTypeConverter;

import javax.persistence.*;

/**
 * 表字段信息
 */
@Entity
@Table(name = "GEN_TABLE_COLUMN")
@Comment("数据表字段配置表")
public class ColumnDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("主键")
    private Long id;

    /**
     * 关联表定义id
     */
    @Column(name = "TABLE_DEFINITION_ID")
    @Comment("关联表定义id")
    private Long tableDefinitionId;

    /**
     * 数据库字段名
     */
    @Column(name = "COLUMN_NAME")
    @Comment("字段名")
    private String columnName;
    /**
     * 数据库类型
     */
    @Column(name = "TYPE")
    @Comment("数据库类型")
    private String type;
    /**
     * 是否自增
     */
    @Column(name = "IS_IDENTITY")
    @Comment("是否自增")
    private Boolean isIdentity = Boolean.FALSE;
    /**
     * 是否主键
     */
    @Column(name = "IS_PK")
    @Comment("是否主键")
    private Boolean isPk = Boolean.FALSE;
    /**
     * 字段注释
     */
    @Column(name = "COMMENT")
    @Comment("字段注释")
    private String comment;
    /**
     * number类型精度
     */
    @Column(name = "NUMBER_PRECISION")
    @Comment("number类型精度")
    private int numberPrecision;
    /**
     * number类型小数位数精度
     */
    @Column(name = "NUMBER_SCALE")
    @Comment("number类型小数位数精度")
    private int numberScale;

    /**
     * 成员变量名称
     */
    @Column(name = "FIELD_NAME")
    @Comment("成员变量名称")
    private String fieldName;
    /**
     * 成员变量类型
     */
    @Column(name = "FIELD_TYPE")
    @Comment("成员变量类型")
    private String fieldType;

    /**
     * 是否可以插入
     */
    @Column(name = "CAN_INSERT")
    @Comment("是否可以插入")
    private Boolean canInsert = Boolean.TRUE;

    /**
     * 是否可以编辑
     */
    @Column(name = "CAN_EDIT")
    @Comment("是否可以编辑")
    private Boolean canEdit = Boolean.TRUE;

    /**
     * 是否在查询列表中显示
     */
    @Column(name = "IS_IN_LIST")
    @Comment("是否在查询列表中显示")
    private Boolean isInList = Boolean.TRUE;

    /**
     * 是否为查询条件
     */
    @Column(name = "CAN_QUERY")
    @Comment("是否为查询条件")
    private Boolean canQuery = Boolean.TRUE;

    /**
     * 查询方式
     */
    @Column(name = "QUERY_TYPE")
    @Comment("查询条件操作符")
    private String queryType = "=";

    /**
     * 显示控件类型
     */
    @Column(name = "WIDGET_TYPE")
    @Comment("显示控件类型")
    private String widgetType;

    /**
     * 显示名称
     */
    @Column(name = "WIDGET_LABEL")
    @Comment("显示名称")
    private String widgetLabel;

    /**
     * 字典名称
     */
    @Column(name = "DICT_NAME")
    @Comment("字典名称")
    private String dictName;

    /**
     * 排序
     */
    @Column(name = "ORDER_NUM")
    @Comment("排序")
    private int orderNum;

    /**
     * 是否是自增主键
     *
     * @return true, 是自增主键
     */
    public boolean getIsIdentityPk() {
        return getIsPk() && getIsIdentity();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTableDefinitionId() {
        return tableDefinitionId;
    }

    public void setTableDefinitionId(Long tableDefinitionId) {
        this.tableDefinitionId = tableDefinitionId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsIdentity() {
        return isIdentity;
    }

    public void setIsIdentity(Boolean isIdentity) {
        this.isIdentity = isIdentity;
    }

    public Boolean getIsPk() {
        return isPk;
    }

    public void setIsPk(Boolean isPk) {
        this.isPk = isPk;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getNumberPrecision() {
        return numberPrecision;
    }

    public void setNumberPrecision(int numberPrecision) {
        this.numberPrecision = numberPrecision;
    }

    public int getNumberScale() {
        return numberScale;
    }

    public void setNumberScale(int numberScale) {
        this.numberScale = numberScale;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Boolean getCanInsert() {
        return canInsert;
    }

    public void setCanInsert(Boolean canInsert) {
        this.canInsert = canInsert;
    }

    public Boolean getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(Boolean canEdit) {
        this.canEdit = canEdit;
    }

    public Boolean getInList() {
        return isInList;
    }

    public void setInList(Boolean inList) {
        isInList = inList;
    }

    public Boolean getCanQuery() {
        return canQuery;
    }

    public void setCanQuery(Boolean canQuery) {
        this.canQuery = canQuery;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(String widgetType) {
        this.widgetType = widgetType;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getWidgetLabel() {
        return widgetLabel;
    }

    public void setWidgetLabel(String widgetLabel) {
        this.widgetLabel = widgetLabel;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }
}
