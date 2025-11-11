package org.sonic.codegen.entity;


import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.sonic.codegen.config.Comment;

/**
 * 模板表
 */
@Entity
@Table(name = "TEMPLATE_CONFIG")
@Comment("模板表")
public class TemplateConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("主键")
	private Long id;
	/** 模板组id */
	@Column(name = "GROUP_ID")
	@Comment("模板组id")
	private Long groupId;
	/** 模板组名称 */
	@Column(name = "GROUP_NAME")
	@Comment("模板组名称")
	private String groupName;
    /** 模板名称 */
	@Column(name = "NAME")
	@Comment("模板名称")
	private String name;
    /** 文件名称 */
	@Column(name = "FILE_NAME")
	@Comment("文件名称")
	private String fileName;
    /** 内容 */
	@Lob
	@Column(name = "CONTENT")
	@Comment("内容")
	private String content;
    /** 是否删除，1：已删除，0：未删除 */
	@Column(name = "DELETED")
	@Comment("是否删除，1：已删除，0：未删除")
	private boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemplateConfig that = (TemplateConfig) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(groupId, that.groupId) &&
                Objects.equals(groupName, that.groupName) &&
                Objects.equals(name, that.name) &&
                Objects.equals(fileName, that.fileName) &&
                Objects.equals(content, that.content) &&
                Objects.equals(deleted, that.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, groupName, name, fileName, content, deleted);
    }

    @Override
    public String toString() {
        return "TemplateConfig{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                ", content='" + content + '\'' +
                ", deleted=" + deleted +
                '}';
    }

}