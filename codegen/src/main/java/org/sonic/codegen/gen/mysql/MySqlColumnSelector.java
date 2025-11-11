package org.sonic.codegen.gen.mysql;

import java.util.Map;
import java.util.Set;

import org.sonic.codegen.entity.ColumnDefinition;
import org.sonic.codegen.gen.ColumnSelector;
import org.sonic.codegen.gen.GeneratorConfig;
import org.sonic.codegen.gen.TypeFormatter;

/**
 * mysql表信息查询
 *
 */
public class MySqlColumnSelector extends ColumnSelector {

	private static final TypeFormatter TYPE_FORMATTER = new MySqlTypeFormatter();

	private static final String SHOW_SQL = "SHOW FULL COLUMNS FROM `%s`";

	public MySqlColumnSelector(GeneratorConfig generatorConfig) {
		super(generatorConfig);
	}

	/**
	 * SHOW FULL COLUMNS FROM 表名
	 */
	@Override
	protected String getColumnInfoSQL(String tableName) {
		return String.format(SHOW_SQL, tableName);
	}
	
	/*
	 * {FIELD=username, EXTRA=, COMMENT=用户名, COLLATION=utf8_general_ci, PRIVILEGES=select,insert,update,references, KEY=PRI, NULL=NO, DEFAULT=null, TYPE=varchar(20)}
	 */
	@Override
	protected ColumnDefinition buildColumnDefinition(Map<String, Object> rowMap){
		Set<String> columnSet = rowMap.keySet();
		
		for (String columnInfo : columnSet) {
			rowMap.put(columnInfo.toUpperCase(), rowMap.get(columnInfo));
		}
		
		ColumnDefinition columnDefinition = new ColumnDefinition();
		
		columnDefinition.setColumnName((String)rowMap.get("FIELD"));
		
		boolean isIdentity = "auto_increment".equalsIgnoreCase((String)rowMap.get("EXTRA"));
		columnDefinition.setIsIdentity(isIdentity);
		
		boolean isPk = "PRI".equalsIgnoreCase((String)rowMap.get("KEY"));
		columnDefinition.setIsPk(isPk);

		columnDefinition.setComment((String)rowMap.get("COMMENT"));
		
		String type = (String)rowMap.get("TYPE");
		columnDefinition.setType(TYPE_FORMATTER.format(type, columnDefinition));
		if("varchar".equalsIgnoreCase(columnDefinition.getType()) && type.contains("(")){
			String lengthStr = type.substring(type.indexOf("(") + 1, type.length() - 1);
			columnDefinition.setNumberPrecision(Integer.parseInt(lengthStr));
		}
		
		return columnDefinition;
	}

}
