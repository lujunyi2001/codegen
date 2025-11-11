package org.sonic.codegen.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sonic.codegen.config.Comment;

/**
 * 数据源配置表
 */
@Entity
@Table(name = "DATASOURCE_CONFIG")
@Comment("数据源配置表")
public class DatasourceConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("主键")
	private Long id;
	/** 显示名称 */
	@Column(name = "DISPLAY_NAME")
	@Comment("显示名称")
	private String displayName;
	/** 数据库类型，1：MySql, 2:Oracle, 3:sqlserver, 4:postgreSql */
	@Column(name = "DB_TYPE")
	@Comment("数据库类型")
	private Integer dbType;
	/** 数据库驱动 */
	@Column(name = "DRIVER_CLASS")
	@Comment("数据库驱动")
	private String driverClass;
	/** 数据库名称 */
	@Column(name = "DB_NAME")
	@Comment("数据库名称")
	private String dbName;
	/** 数据库host */
	@Column(name = "HOST")
	@Comment("数据库host")
	private String host;
	/** 数据库端口 */
	@Column(name = "PORT")
	@Comment("数据库端口")
	private Integer port;
	/** 数据库用户名 */
	@Column(name = "USERNAME")
	@Comment("数据库用户名")
	private String username;
	/** 数据库密码 */
	@Column(name = "PASSWORD")
	@Comment("数据库密码")
	private String password;
	/** 是否已删除，1：已删除，0：未删除 */
	@Column(name = "DELETED", updatable = false)
	@Comment("是否已删除，1：已删除，0：未删除")
	private boolean deleted;
	/** 包名 */
	@Column(name = "PACKAGE_NAME_PREFIX")
	@Comment("包名")
	private String packageNamePrefix;
	/** 删除的前缀 */
	@Column(name = "DEL_PREFIX")
	@Comment("删除的前缀")
	private String delPrefix;
	/** 代码生成器模板组id */
	@Column(name = "GROUPID")
	@Comment("代码生成器模板组id")
	private String groupId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDbType() {
		return dbType;
	}

	public void setDbType(Integer dbType) {
		this.dbType = dbType;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DatasourceConfig that = (DatasourceConfig) o;
		return Objects.equals(id, that.id) && Objects.equals(dbType, that.dbType)
				&& Objects.equals(driverClass, that.driverClass) && Objects.equals(dbName, that.dbName)
				&& Objects.equals(host, that.host) && Objects.equals(port, that.port)
				&& Objects.equals(username, that.username) && Objects.equals(password, that.password)
				&& Objects.equals(deleted, that.deleted) && Objects.equals(packageNamePrefix, that.packageNamePrefix)
				&& Objects.equals(delPrefix, that.delPrefix) && Objects.equals(groupId, that.groupId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, dbType, driverClass, dbName, host, port, username, password, deleted, packageNamePrefix,
				delPrefix, groupId);
	}

	@Override
	public String toString() {
		return "DatasourceConfig{" + "id=" + id + ", dbType=" + dbType + ", driverClass='" + driverClass + '\''
				+ ", dbName='" + dbName + '\'' + ", host='" + host + '\'' + ", port=" + port + ", username='" + username
				+ '\'' + ", password='" + password + '\'' + ", deleted=" + deleted + ", packageName='" + packageNamePrefix
				+ '\'' + ", delPrefix='" + delPrefix + '\'' + ", groupId='" + groupId + '\'' + '}';
	}
}