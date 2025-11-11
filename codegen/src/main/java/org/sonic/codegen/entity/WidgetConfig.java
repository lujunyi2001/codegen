package org.sonic.codegen.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sonic.codegen.config.Comment;

import java.util.Objects;

/**
 * 模板组配置类
 */
@Entity
@Table(name = "WIDGET_CONFIG")
@Comment("显示控件配置")
public class WidgetConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("主键")
	private Long id;
	/** 显示名称 */
	@Column(name = "CODE")
	@Comment("控件类型代码")
	private String code;
	/** 显示名称 */
	@Column(name = "DISPLAY_NAME")
	@Comment("控件类型名称")
	private String name;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) {return false;}
        WidgetConfig that = (WidgetConfig) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }

    @Override
    public String toString() {
        return "WidgetConfig{" +
                "id=" + id +
                ",code='" + code + "'" +
                ",name='" + name + "'" +
                '}';
    }


}