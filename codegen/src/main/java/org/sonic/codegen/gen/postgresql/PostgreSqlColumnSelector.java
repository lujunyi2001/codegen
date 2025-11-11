package org.sonic.codegen.gen.postgresql;


import java.util.Map;
import java.util.Set;

import org.sonic.codegen.entity.ColumnDefinition;
import org.sonic.codegen.gen.ColumnSelector;
import org.sonic.codegen.gen.GeneratorConfig;
import org.sonic.codegen.util.FieldUtil;

/**
 * @author tanghc
 */
public class PostgreSqlColumnSelector extends ColumnSelector {

    private static final PostgreSqlTypeFormatter SQL_TYPE_FORMATTER = new PostgreSqlTypeFormatter();

    public PostgreSqlColumnSelector(GeneratorConfig dataBaseConfig) {
        super(dataBaseConfig);
    }

    private static final String SHOW_COLUMN_SQL = "SELECT  " +
            " pg_attribute.attname AS colname,  " +
            " atttypid::regtype AS type,  " +
            " col_description ( pg_attribute.attrelid, pg_attribute.attnum ) AS cmt,  " +
            " pg_attribute.attnum = pg_constraint.conkey [ 1 ] AS is_pk,  " +
            "CASE WHEN POSITION ( 'nextval' IN column_default ) > 0 THEN 1 ELSE 0 END AS is_identity  " +
            "FROM  " +
            " pg_constraint  " +
            " INNER JOIN pg_class ON pg_constraint.conrelid = pg_class.oid  " +
            " INNER JOIN pg_attribute ON pg_attribute.attrelid = pg_class.oid  " +
            " INNER JOIN pg_type ON pg_type.oid = pg_attribute.atttypid  " +
            " INNER JOIN information_schema.COLUMNS C ON C.TABLE_NAME = pg_class.relname   " +
            " AND C.COLUMN_NAME = pg_attribute.attname   " +
            "WHERE  " +
            " pg_class.relname = '%s'   " +
            " AND pg_constraint.contype = 'p'   " +
            " AND pg_attribute.attnum > 0";

    @Override
    protected String getColumnInfoSQL(String tableName) {
        return String.format(SHOW_COLUMN_SQL, tableName);
    }

    @Override
    protected ColumnDefinition buildColumnDefinition(Map<String, Object> rowMap) {
        Set<String> columnSet = rowMap.keySet();
        for (String columnInfo : columnSet) {
            rowMap.put(columnInfo.toUpperCase(), rowMap.get(columnInfo));
        }

        ColumnDefinition columnDefinition = new ColumnDefinition();

        columnDefinition.setColumnName(FieldUtil.convertString(rowMap.get("COLNAME")));

        boolean isIdentity = "1".equals(FieldUtil.convertString(rowMap.get("IS_IDENTITY")));
        columnDefinition.setIsIdentity(isIdentity);

        boolean isPk = (Boolean) rowMap.get("IS_PK");
        columnDefinition.setIsPk(isPk);
        
        columnDefinition.setComment(FieldUtil.convertString(rowMap.get("CMT")));

        String type = FieldUtil.convertString(rowMap.get("TYPE"));
        columnDefinition.setType(SQL_TYPE_FORMATTER.format(type, columnDefinition));


        return columnDefinition;
    }

}
