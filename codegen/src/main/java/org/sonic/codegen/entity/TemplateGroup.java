package org.sonic.codegen.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sonic.codegen.config.Comment;

/**
 * 模板组配置类
 */
@Entity
@Table(name = "TEMPLATE_GROUP")
@Comment("模板组配置类")
public class TemplateGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("主键")
	private Long id;
	/** 显示名称 */
	@Column(name = "DISPLAY_NAME")
	@Comment("显示名称")
	private String groupName;
	/** 是否已删除，1：已删除，0：未删除 */
	@Column(name = "DELETED", updatable = false)
	@Comment("是否已删除，1：已删除，0：未删除")
	private boolean deleted;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return this.groupName;
    }
    
    public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}


    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) {return false;}
        TemplateGroup that = (TemplateGroup) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TemplateGroup{" +
                "id=" + id +
                ",groupName='" + groupName + "'" +
                ",deleted='" + deleted + "'" +
                '}';
    }

}