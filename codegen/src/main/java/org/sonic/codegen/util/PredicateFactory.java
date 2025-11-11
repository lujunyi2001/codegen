package org.sonic.codegen.util;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

public class PredicateFactory {
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Predicate build(Root<?> root, 
            CriteriaBuilder builder, ComplexQueryParamVo vo, PropertyDescriptor desc) {
		Class<?> propertyType = desc.getPropertyType();
		Object value = null;
		if(!ComplexQueryParamVo.OP_IN.equals(vo.getOp())) {
			value = ConvertFactory.convertToObject(vo.getValue(), propertyType);
			if(value == null) {
				return null;
			}
		}
		
		
		Predicate predicate = null;
		Path expression = null;  
		String fieldName = vo.getName();
		if(fieldName .contains(".")){  
            String[] names = StringUtils.split(fieldName, ".");  
            expression = root.get(names[0]);  
            for (int i = 1; i < names.length; i++) {  
                expression = expression.get(names[i]);  
            }  
        }else{  
            expression = root.get(fieldName);  
        }  
		
		switch (vo.getOp()) {
		case ComplexQueryParamVo.OP_EQ:
			predicate = builder.equal(expression, value);
			break;
		case ComplexQueryParamVo.OP_NE:
			predicate = builder.notEqual(expression, value);
			break;
		case ComplexQueryParamVo.OP_LIKE:
			if(propertyType == String.class) {
				predicate = builder.like((Expression<String>) expression, "%" + value + "%");
			}
			break;
		case ComplexQueryParamVo.OP_LIKESTART:
			if(propertyType == String.class) {
				predicate = builder.like((Expression<String>) expression, value + "%");
			}
			break;
		case ComplexQueryParamVo.OP_LIKEEND:
			if(propertyType == String.class) {
				predicate = builder.like((Expression<String>) expression, "%" + value);
			}
			break;
		case ComplexQueryParamVo.OP_GE:
			predicate = builder.greaterThanOrEqualTo(expression, (Comparable) value);  
			break;
		case ComplexQueryParamVo.OP_GT:
			predicate = builder.greaterThan(expression, (Comparable)value);
			break;
		case ComplexQueryParamVo.OP_LE:
			predicate = builder.lessThanOrEqualTo(expression, (Comparable)value);
			break;
		case ComplexQueryParamVo.OP_LT:
			predicate = builder.lessThan(expression, (Comparable)value);
			break;
		case ComplexQueryParamVo.OP_IN:
			if(StringUtils.isNotBlank(vo.getValue())) {
				List<Object> list = new ArrayList<>();
				
				String[] split = vo.getValue().split(",");
				Object tmpVal = null;
				for (int i = 0; i < split.length; i++) {
					tmpVal = ConvertFactory.convertToObject(split[i], propertyType);
					if(tmpVal != null) {
						list.add(tmpVal);
					}
				}
				if(!list.isEmpty()) {
					In in = builder.in(expression);
					for (Object object : list) {
						in.value(object);
					}
					predicate = in;
				}
			}
			break;

		default:
			break;
		}
		return predicate;
	}
	public static void buildHql(ComplexQueryParamVo vo, PropertyDescriptor desc, 
			List<String> whereList, List<String> paramNames, List<Object> paramValues) {
		Class<?> propertyType = desc.getPropertyType();
		Object value = null;
		if(!ComplexQueryParamVo.OP_IN.equals(vo.getOp()) && 
				!ComplexQueryParamVo.OP_ISNULL.equals(vo.getOp()) &&
				!ComplexQueryParamVo.OP_ISNOTNULL.equals(vo.getOp())) {
			value = ConvertFactory.convertToObject(vo.getValue(), propertyType);
			if(value == null) {
				return ;
			}
		}
		
		String fieldName = vo.getName();
		String paramName = vo.generateParamName();
		boolean isEffect = true;
		switch (vo.getOp()) {
		case ComplexQueryParamVo.OP_EQ:
			whereList.add(fieldName + "=:" + paramName);
			break;
		case ComplexQueryParamVo.OP_NE:
			whereList.add(fieldName + "<>:" + paramName);
			break;
		case ComplexQueryParamVo.OP_LIKE:
			if(propertyType == String.class) {
				whereList.add(fieldName + " like :" + paramName);
				paramNames.add(paramName);
				paramValues.add("%" + value + "%");
			}
			isEffect = false;
			break;
		case ComplexQueryParamVo.OP_LIKESTART:
			if(propertyType == String.class) {
				whereList.add(fieldName + " like :" + paramName);
				paramNames.add(paramName);
				paramValues.add(value + "%");
			}
			isEffect = false;
			break;
		case ComplexQueryParamVo.OP_LIKEEND:
			if(propertyType == String.class) {
				whereList.add(fieldName + " like :" + paramName);
				paramNames.add(paramName);
				paramValues.add("%" + value);
			}
			isEffect = false;
			break;
		case ComplexQueryParamVo.OP_GE:
			whereList.add(fieldName + " >= :" + paramName);
			break;
		case ComplexQueryParamVo.OP_GT:
			whereList.add(fieldName + " > :" + paramName);
			break;
		case ComplexQueryParamVo.OP_LE:
			whereList.add(fieldName + " <= :" + paramName);
			break;
		case ComplexQueryParamVo.OP_LT:
			whereList.add(fieldName + " < :" + paramName);
			break;
		case ComplexQueryParamVo.OP_IN:
			if(StringUtils.isNotBlank(vo.getValue())) {
				List<Object> list = new ArrayList<>();
				
				String[] split = vo.getValue().split(",");
				Object tmpVal = null;
				for (int i = 0; i < split.length; i++) {
					tmpVal = ConvertFactory.convertToObject(split[i], propertyType);
					if(tmpVal != null) {
						list.add(tmpVal);
					}
				}
				if(!list.isEmpty()) {
					whereList.add(fieldName + " in (:" + paramName + ")");
					paramNames.add(paramName);
					paramValues.add(list);
				}
			}
			isEffect = false;
			break;
		case ComplexQueryParamVo.OP_ISNULL:
			whereList.add(fieldName + " is null");
			isEffect = false;
			break;
		case ComplexQueryParamVo.OP_ISNOTNULL:
			whereList.add(fieldName + " is not null");
			isEffect = false;
			break;
			
		default:
			break;
		}
		if(isEffect) {
			paramNames.add(paramName);
			paramValues.add(value);
		}
	}
	
}
