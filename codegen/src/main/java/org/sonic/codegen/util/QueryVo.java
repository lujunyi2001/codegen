package org.sonic.codegen.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class QueryVo<T> {

	private String[] where;
	private String[] groupBy;
	private Class<T> mapClass;
	private Map<String, String> selectKey = new LinkedHashMap<>();
	
	public void addSelectKey(String select, String alias) {
		selectKey.put(select, alias);
	}
	
	public String[] getWhere() {
		return where;
	}
	public void setWhere(String... where) {
		this.where = where;
	}
	public Class<T> getMapClass() {
		return mapClass;
	}
	public void setMapClass(Class<T> mapClass) {
		this.mapClass = mapClass;
	}
	public String[] getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(String... groupBy) {
		this.groupBy = groupBy;
	}

	public Map<String, String> getSelectKey() {
		return selectKey;
	}
}
