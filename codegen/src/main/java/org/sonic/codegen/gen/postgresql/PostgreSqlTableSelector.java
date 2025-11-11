package org.sonic.codegen.gen.postgresql;

import java.util.List;
import java.util.Map;

import org.sonic.codegen.gen.ColumnSelector;
import org.sonic.codegen.gen.GeneratorConfig;
import org.sonic.codegen.entity.TableDefinition;
import org.sonic.codegen.gen.TableSelector;
import org.sonic.codegen.util.FieldUtil;

/**
 * @author tanghc
 */
public class PostgreSqlTableSelector extends TableSelector {
    public PostgreSqlTableSelector(ColumnSelector columnSelector, GeneratorConfig generatorConfig) {
        super(columnSelector, generatorConfig);
    }

    private static String SHOW_TABLE_SQL =
            "SELECT relname, " +
                "obj_description(oid) AS cmt " +
            "FROM pg_class C " +
            "WHERE relkind='r' AND relname NOT LIKE 'pg_%%' AND relname NOT LIKE 'sql_%%' AND relchecks=0 " +
            "%s " +
            "ORDER BY relname";

    @Override
    protected String getShowTablesSQL(String showParam) {
        List<String> tableNames = wrapTableNames();
        String and = "";
        if (!tableNames.isEmpty()) {
            and = String.format("AND relname IN (%s)  ", String.join(",", tableNames));
        }
        return String.format(SHOW_TABLE_SQL, and);
    }

    @Override
    protected TableDefinition buildTableDefinition(Map<String, Object> tableMap) {
        TableDefinition tableDefinition = new TableDefinition();
        tableDefinition.setTableName(FieldUtil.convertString(tableMap.get("RELNAME")));
        tableDefinition.setComment(FieldUtil.convertString(tableMap.get("CMT")));
        return tableDefinition;
    }
}
