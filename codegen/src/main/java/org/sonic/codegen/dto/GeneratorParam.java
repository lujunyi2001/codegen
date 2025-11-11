package org.sonic.codegen.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


public class GeneratorParam {
	/** datasource_config主键 */
	@NotNull(message = "数据源id不能为空")
	private Long datasourceConfigId;

	@NotEmpty(message = "表名不能为空")
	/** 表名 */
	private List<String> tableNames;
	/** 表表定义id */
	private List<Long> tableDefinitionIds;

	/** template_config主键 */
	private List<Long> templateConfigIdList;

	private String packageNamePrefix;

	private String delPrefix;

	private String charset = "UTF-8";

	public Long getDatasourceConfigId() {
		return datasourceConfigId;
	}

	public void setDatasourceConfigId(Long datasourceConfigId) {
		this.datasourceConfigId = datasourceConfigId;
	}

	public List<String> getTableNames() {
		return tableNames;
	}

	public void setTableNames(List<String> tableNames) {
		this.tableNames = tableNames;
	}

	public List<Long> getTemplateConfigIdList() {
		return templateConfigIdList;
	}

	public void setTemplateConfigIdList(List<Long> templateConfigIdList) {
		this.templateConfigIdList = templateConfigIdList;
	}

	public String getPackageNamePrefix() {
		return packageNamePrefix;
	}

	public void setPackageNamePrefix(String packageNamePrefix) {
		this.packageNamePrefix = packageNamePrefix;
	}

	public String getDelPrefix() {
		return delPrefix;
	}

	public void setDelPrefix(String delPrefix) {
		this.delPrefix = delPrefix;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public List<Long> getTableDefinitionIds() {
		return tableDefinitionIds;
	}

	public void setTableDefinitionIds(List<Long> tableDefinitionIds) {
		this.tableDefinitionIds = tableDefinitionIds;
	}

	@Override
	public String toString() {
		return "GeneratorParam{" +
				"datasourceConfigId=" + datasourceConfigId +
				", tableNames=" + tableNames +
				", tableDefinitionIds=" + tableDefinitionIds +
				", templateConfigIdList=" + templateConfigIdList +
				", packageName='" + packageNamePrefix + '\'' +
				", delPrefix='" + delPrefix + '\'' +
				", charset='" + charset + '\'' +
				'}';
	}
}
