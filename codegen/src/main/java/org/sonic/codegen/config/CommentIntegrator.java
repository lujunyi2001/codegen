package org.sonic.codegen.config;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Selectable;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.sonicframework.utils.ClassUtil;
import org.springframework.stereotype.Component;

@Component
public class CommentIntegrator implements Integrator {

	public final static CommentIntegrator INSTANCE = new CommentIntegrator();
	private CommentIntegrator() {
	}
	@Override
	public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory,
			SessionFactoryServiceRegistry serviceRegistry) {
		Collection<PersistentClass> entityBindings = metadata.getEntityBindings();
		for (PersistentClass persistentClass : entityBindings) {
			addComment(persistentClass);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void addComment(PersistentClass persistentClass) {
		try {
			Class<?> clazz = Class.forName(persistentClass.getClassName());
			Map<String, String> fieldComment = getMethodComment(clazz);
			fieldComment.putAll(getFieldComment(clazz));
			
			
			PersistentClass parseClass = persistentClass;
			while(parseClass != null) {
				clazz = Class.forName(parseClass.getClassName());
				if(clazz.isAnnotationPresent(Comment.class)) {
					String comment = clazz.getAnnotation(Comment.class).value();
					if(StringUtils.isNotBlank(comment)) {
						parseClass.getTable().setComment(comment);
					}
				}
				fieldComment.putAll(getFieldComment(clazz));
				Property property = parseClass.getDeclaredIdentifierProperty();
				if(property != null) {
					setProperComment(property, fieldComment);
				}
				Iterator<Property> iterator = parseClass.getDeclaredPropertyIterator();
				while (iterator.hasNext()) {
					property = iterator.next();
					setProperComment(property, fieldComment);
				}
				 iterator = parseClass.getUnjoinedPropertyIterator();
					while (iterator.hasNext()) {
						property = iterator.next();
						setProperComment(property, fieldComment);
					}
				parseClass = parseClass.getSuperclass();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setProperComment(Property property, Map<String, String> fieldComment) {
		if(property == null) {
			return ;
		}
		String name = property.getName();
		
		if(fieldComment.containsKey(name)) {
			Iterator<Selectable> iterator = property.getValue().getColumnIterator();
			Selectable selectable = null;
			while (iterator.hasNext()) {
				selectable = iterator.next();
				if(selectable instanceof Column) {
					((Column)selectable).setComment(fieldComment.get(name));
				}
			}
		}
	}
	
	private Map<String, String> getFieldComment(Class<?> clazz){
		Map<String, String> map = new HashMap<>();
		String value = null;
		Map<String, Comment> commentsMap = ClassUtil.findAnnotationByClassFields(clazz, Comment.class);
		for (Map.Entry<String, Comment> entry : commentsMap.entrySet()) {
			value = entry.getValue().value();
			if(StringUtils.isNotBlank(value)) {
				map.put(entry.getKey(), value);
			}
		}
		return map;
	}
	private Map<String, String> getMethodComment(Class<?> clazz){
		Map<String, String> map = new HashMap<>();
		Method[] methods = clazz.getDeclaredMethods();
		String value = null;
		for (int i = 0; i < methods.length; i++) {
			if(methods[i].isAnnotationPresent(Comment.class)) {
				value = methods[i].getAnnotation(Comment.class).value();
				if(StringUtils.isNotBlank(value)) {
					map.put(convertMethodName(methods[i].getName()), value);
				}
			}
		}
		return map;
	}
	
	private final static String METHOD_PREFIX_SET = "set";
	private final static String METHOD_PREFIX_GET = "get";
	private final static String METHOD_PREFIX_IS = "is";
	private String convertMethodName(String name) {
		if(name == null) {
			return null;
		}else if(name.startsWith(METHOD_PREFIX_SET)) {
			name = name.substring(METHOD_PREFIX_SET.length());
		}else if(name.startsWith(METHOD_PREFIX_GET)) {
			name = name.substring(METHOD_PREFIX_GET.length());
		}else if(name.startsWith(METHOD_PREFIX_IS)) {
			name = name.substring(METHOD_PREFIX_IS.length());
		}
		if(name.length() > 0) {
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
		}
		return name;
	}

	@Override
	public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
	}

}
