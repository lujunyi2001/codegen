package org.sonic.codegen.entity;

import org.sonic.codegen.config.Comment;

import javax.persistence.*;
import java.util.Objects;

/**
 * 模板组配置类
 */
@Entity
@Table(name = "QUERY_TYPE_CONFIG")
@Comment("查询类型配置")
public class QueryTypeConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("主键")
	private Long id;
	/** 显示名称 */
	@Column(name = "CODE")
	@Comment("查询类型")
	private String code;
	/** 显示名称 */
	@Column(name = "DISPLAY_NAME")
	@Comment("查询类型名称")
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
        QueryTypeConfig that = (QueryTypeConfig) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }

    @Override
    public String toString() {
        return "QueryTypeConfig{" +
                "id=" + id +
                ",code='" + code + "'" +
                ",name='" + name + "'" +
                '}';
    }


}