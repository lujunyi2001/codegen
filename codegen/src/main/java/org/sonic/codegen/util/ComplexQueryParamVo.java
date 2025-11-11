package org.sonic.codegen.util;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ComplexQueryParamVo {

	public static final String OP_EQ = "eq";
	public static final String OP_NE = "ne";
	public static final String OP_LT = "lt";
	public static final String OP_LE = "le";
	public static final String OP_GT = "gt";
	public static final String OP_GE = "ge";
	public static final String OP_LIKE = "like";
	public static final String OP_LIKESTART = "likeStart";
	public static final String OP_LIKEEND = "likeEnd";
	public static final String OP_IN = "in";
	public static final String OP_ISNULL = "null";
	public static final String OP_ISNOTNULL = "notnull";
	public static final String OP_LIKESTART_IN = "likeStartIn";
	public static final String OP_LIKE_IN = "likeIn";
	
	
	private String op = OP_EQ;
	private String name;
	private String value;
	private String groupName;
	private PropertyDescriptor descriptor;
	private static AtomicInteger generator = new AtomicInteger(0);
	private final static String PARAM_PREFIX = "param";
	private final static String PARAM_PREFIX_GEN = "G";
	private int id = -1;
	private List<ComplexQueryParamVo> subVoList = null;
	
	public ComplexQueryParamVo() {
		super();
	}
	public ComplexQueryParamVo(String name, String value) {
		this(name, value, OP_EQ);
	}
	public String getOp() {
		return op;
	}
	public ComplexQueryParamVo setOp(String op) {
		this.op = op;
		return this;
	}
	public String getName() {
		return name;
	}
	public ComplexQueryParamVo setName(String name) {
		this.name = name;
		return this;
	}
	public String getValue() {
		return value;
	}
	public ComplexQueryParamVo setValue(String value) {
		this.value = value;
		return this;
	}
	public ComplexQueryParamVo(String name, String value, String op) {
		super();
		this.op = op;
		this.name = name;
		this.value = value;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public PropertyDescriptor getDescriptor() {
		return descriptor;
	}
	public void setDescriptor(PropertyDescriptor descriptor) {
		this.descriptor = descriptor;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	public String generateParamName() {
		if(id >= 0) {
			return PARAM_PREFIX + id;
		}
		int gen = generator.getAndIncrement();
		if(gen < 0) {
			synchronized (ComplexQueryParamVo.class) {
				if(generator.get() <0) {
					generator.set(0);
				}
				gen = generator.getAndIncrement();
			}
		}
		return PARAM_PREFIX + PARAM_PREFIX_GEN + gen;
	}
	public List<ComplexQueryParamVo> getSubVoList() {
		return subVoList;
	}
	public void setSubVoList(List<ComplexQueryParamVo> subVoList) {
		this.subVoList = subVoList;
	}
	
}
