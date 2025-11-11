package org.sonic.codegen.gen.sqlserver;

import java.util.Map;

import org.sonic.codegen.gen.ColumnSelector;
import org.sonic.codegen.gen.GeneratorConfig;
import org.sonic.codegen.entity.TableDefinition;
import org.sonic.codegen.gen.TableSelector;

public class SqlServerTableSelector extends TableSelector {
	
	public SqlServerTableSelector(ColumnSelector columnSelector,
                                  GeneratorConfig dataBaseConfig) {
		super(columnSelector, dataBaseConfig);
	}

	@Override
	protected String getShowTablesSQL(String dbName) {
		return "SELECT SS.name + '.' + t.name AS table_name " +
				",ISNULL(cast(ext.value as varchar(500)), '') as comment " +
				"FROM sysobjects t " +
				"INNER JOIN sys.objects SO ON t.name = SO.name " +
				"INNER JOIN sys.schemas  SS ON SO.schema_id = SS.schema_id " +
				"LEFT JOIN sys.extended_properties ext ON ext.major_id = SO.object_id and ext.minor_id=0 " +
				"WHERE t.xtype='u' " +
				this.buildTableSchWhere() +
				" ORDER BY SS.name ASC,t.name ASC";
	}
	
	// and ( (t.name = 'bar' and SS.name = 'front') or (t.name = 'adjustBatch' and SS.name = 'account') )
	private String buildTableSchWhere() {
		if(this.getSchTableNames() != null && this.getSchTableNames().size() > 0) {
			int i = 0;
			StringBuilder tables = new StringBuilder(" and ( ");
			for (String table : this.getSchTableNames()) {
				String[] tableArr = table.split("\\.");
				if(i > 0) {
					tables.append(" or ");
				}
				tables.append("(SS.name='").append(tableArr[0]).append("' and t.name='").append(tableArr[1]).append("') ");
				i++;
			}
			tables.append(" )");
			return tables.toString();
		}
		return "";
	}

	// {TABLE_NAME=account.adjustBatch, COMMENT=}
	@Override
	protected TableDefinition buildTableDefinition(Map<String, Object> tableMap) {
		TableDefinition tableDefinition = new TableDefinition();
		tableDefinition.setTableName((String)tableMap.get("TABLE_NAME"));
		tableDefinition.setComment((String)tableMap.get("COMMENT"));
		return tableDefinition;
	}

}
