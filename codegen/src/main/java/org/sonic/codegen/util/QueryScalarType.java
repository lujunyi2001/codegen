package org.sonic.codegen.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.type.AbstractStandardBasicType;
import org.hibernate.type.StandardBasicTypes;

public class QueryScalarType {

	private static Map<Class<?>, AbstractStandardBasicType<?>> typeMap = new HashMap<>();
	
	static {
		typeMap.put(int.class, StandardBasicTypes.INTEGER);
		typeMap.put(Integer.class, StandardBasicTypes.INTEGER);
		typeMap.put(short.class, StandardBasicTypes.SHORT);
		typeMap.put(Short.class, StandardBasicTypes.SHORT);
		typeMap.put(boolean.class, StandardBasicTypes.BOOLEAN);
		typeMap.put(Boolean.class, StandardBasicTypes.BOOLEAN);
		typeMap.put(long.class, StandardBasicTypes.LONG);
		typeMap.put(Long.class, StandardBasicTypes.LONG);
		typeMap.put(double.class, StandardBasicTypes.DOUBLE);
		typeMap.put(Double.class, StandardBasicTypes.DOUBLE);
		typeMap.put(float.class, StandardBasicTypes.FLOAT);
		typeMap.put(Float.class, StandardBasicTypes.FLOAT);
		typeMap.put(char.class, StandardBasicTypes.CHARACTER);
		typeMap.put(Character.class, StandardBasicTypes.CHARACTER);
		typeMap.put(byte.class, StandardBasicTypes.BYTE);
		typeMap.put(Byte.class, StandardBasicTypes.BYTE);
		typeMap.put(Integer.class, StandardBasicTypes.INTEGER);
		typeMap.put(Integer.class, StandardBasicTypes.INTEGER);
		typeMap.put(String.class, StandardBasicTypes.STRING);
		typeMap.put(Date.class, StandardBasicTypes.DATE);
	}
	
	public static AbstractStandardBasicType<?> getType(Class<?> clazz) {
		return typeMap.get(clazz);
	}
	
}
