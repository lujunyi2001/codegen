package org.sonic.codegen.util;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.internal.QueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.AbstractStandardBasicType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonicframework.utils.ClassUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public class ComplexQueryUtil {
	
	public final static String PAGE_QUERY_START_INDEX = "pageQueryStartIndex";
	public final static String PAGE_QUERY_ONLY_TOTAL = "pageQueryOnlyTotal";
	public final static String PAGE_TOTAL_COUNT = "pageTotalCount";
	public final static String PAGE_NUM_KEY = "pageNum";
	public final static String PAGE_SIZE_KEY = "pageSize";
	public final static String PAGE_SORT = "sort";
	public final static String PAGE_SORT_ASC = "asc";
	public final static String PAGE_SORT_DESC = "desc";
	
	private static Map<String, PropertyDescriptor> propertyDescriptorMap = new ConcurrentHashMap<>();
	
	private static Logger log = LoggerFactory.getLogger(ComplexQueryUtil.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static<T, K> Page<T> queryData(EntityManager entityManager, Map<String, String> paramMap, Class<T> clazz, String...leftjoinName) {
		boolean queryOnlyTotal = Boolean.valueOf(paramMap.get(PAGE_QUERY_ONLY_TOTAL));
		long pageTotalCount = -1;
		try {
			pageTotalCount = getIntFromMap(paramMap, PAGE_TOTAL_COUNT);
		} catch (Exception e) {
		}finally{
			paramMap.remove(PAGE_TOTAL_COUNT);
		}
		
		Map<String, PropertyDescriptor> descMap = ClassUtil.getClassDesc(clazz);
		String sortHql = parseHqlSort(paramMap, descMap);
		PageRequest pageInfo = buildPageInfo(paramMap, descMap);
		String hql = "%s from " + clazz.getSimpleName() + " t %s where 1=1 ";
		
		List<String> wheres = new ArrayList<>();
		List<String> names = new ArrayList<>();
		List<Object> values = new ArrayList<>();
		buildQueryInfo(clazz, paramMap, descMap, wheres, names, values);
		for (String where : wheres) {
			hql += " and " + (where.trim().startsWith("(")?"":"t.") + where;
		}
		if(pageTotalCount < 0 && pageInfo != null) {
			String countHql = String.format(hql, "select count(1) ", "");
			log.debug("countHql:[{}]", countHql);
			Query countQuery = entityManager.createQuery(countHql);
			setParameter(countQuery, names, values);
			List<Long> list = countQuery.getResultList();
			pageTotalCount = list.get(0);
		}
		for (int i = 0; i < leftjoinName.length; i++) {
			leftjoinName[i] = " left join fetch t." + leftjoinName[i];
		}
		List<T> list = new ArrayList<>();
		PageImpl<T> pageData = null;
		if(!queryOnlyTotal) {
			String selectHql = String.format(hql, "", StringUtils.join(leftjoinName, ","));
			selectHql += sortHql;
			log.debug("selectHql:[{}]", selectHql);
			Query query = entityManager.createQuery(selectHql);
			if(pageInfo != null) {
				int startIndex = getIntFromMap(paramMap, PAGE_QUERY_START_INDEX);
				if(startIndex < 0) {
					startIndex = pageInfo.getPageNumber() * pageInfo.getPageSize();
				}
				query.setFirstResult(startIndex).setMaxResults(pageInfo.getPageSize());
				
			}
			setParameter(query, names, values);
			list = query.getResultList();
		}
		
		
		if(pageInfo != null) {
			pageData = new PageImpl<T>(list, pageInfo, pageTotalCount);
		}else {
			pageData = new PageImpl(list);
		}
		return pageData;
		
	}
	
	private static String parseHqlSort(Map<String, String> param, Map<String, PropertyDescriptor> descMap) {
		String order = "";
		if(param.containsKey(PAGE_SORT)) {
			String sortStr = param.get(PAGE_SORT);
			param.remove(PAGE_SORT);
			if(StringUtils.isNotBlank(sortStr)) {
				String[] split = sortStr.split(",");
				String[] orderArr = new String[split.length];
				String[] sep = null;
				for (int i = 0; i < split.length; i++) {
					if(StringUtils.isBlank(split[i])) {
						continue;
					}
					sep = split[i].trim().split("_");
					if(sep.length > 0) {
						orderArr[i] = "t." + sep[0] + (PAGE_SORT_DESC.equalsIgnoreCase(sep[1])?" desc,":" asc,");
					}
				}
				for (int i = 0; i < orderArr.length; i++) {
					if(StringUtils.isNotBlank(orderArr[i])) {
						order += orderArr[i];
					}
				}
			}
		}
		if(order.length() > 0) {
			order = order.substring(0, order.length() - 1);
			order = " order by " + order;
		}
		return order;
	}
	
	private static void setParameter(Query query, 
			List<String> names, List<Object> values) {
		for (int i = 0; i < names.size(); i++) {
			query.setParameter(names.get(i), values.get(i));
			log.trace("setParameter name:[{}], value:[{}]", names.get(i), values.get(i));
		}
	}
	private static void buildQueryInfo(Class<?> clazz, final Map<String, String> paramMap, final Map<String, PropertyDescriptor> descMap, 
			List<String> wheres, List<String> names, List<Object> values) {
		List<ComplexQueryParamVo> oringinList = new ArrayList<>();
		ComplexQueryParamVo vo = null;
		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			vo = buildComplexQueryParamVo(clazz, entry.getKey(), entry.getValue(), descMap);
			if(vo != null) {
				if(CollectionUtils.isNotEmpty(vo.getSubVoList())) {
					List<ComplexQueryParamVo> subVoList = vo.getSubVoList();
					for (ComplexQueryParamVo subVo : subVoList) {
						if(subVo != null) {
							subVo.setId(oringinList.size());
							oringinList.add(subVo);
						}
					}
				}else {
					vo.setId(oringinList.size());
					oringinList.add(vo);
				}
				
			}
		}
		List<ComplexQueryParamVo> destList = new ArrayList<>();
		Map<String, List<ComplexQueryParamVo>> destGroupMap = new HashMap<>();
		filterGroup(oringinList, destList, destGroupMap);
		for (ComplexQueryParamVo andVo : destList) {
			if(andVo == null) {
				continue;
			}
			PredicateFactory.buildHql(andVo, andVo.getDescriptor(), wheres, names, values);
		}
		
		List<String> tmpWheres = null;
		List<String> tmpNames = null;
		List<Object> tmpValues = null;
		for (List<ComplexQueryParamVo> orList : destGroupMap.values()) {
			if(orList.isEmpty()) {
				continue;
			}
			tmpWheres = new ArrayList<>();
			tmpNames = new ArrayList<>();
			tmpValues = new ArrayList<>();
			for (ComplexQueryParamVo tmpVo : orList) {
				PredicateFactory.buildHql(tmpVo, tmpVo.getDescriptor(), tmpWheres, tmpNames, tmpValues);
			}
			if(tmpWheres.isEmpty()) {
				continue;
			}
			tmpWheres = tmpWheres.stream().map(t->"t." + t).collect(Collectors.toList());
			String thisWhere = " (" + StringUtils.join(tmpWheres, " or ") + ")";
			wheres.add(thisWhere);
			names.addAll(tmpNames);
			values.addAll(tmpValues);
		}
		
//		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
//			buildSingleQueryInfo(clazz, entry, descMap, wheres, names, values);
//		}
	}
	@SuppressWarnings("unused")
	private static void buildSingleQueryInfo(Class<?> clazz, Map.Entry<String, String> entry, final Map<String, PropertyDescriptor> descMap, 
			List<String> wheres, List<String> names, List<Object> values) {
		buildSingleQueryInfo(clazz, entry.getKey(), entry.getValue(), descMap, wheres, names, values);
	}
	@SuppressWarnings("unused")
	private static void buildSingleQueryInfo(Class<?> clazz, String key, String value, 
			final Map<String, PropertyDescriptor> descMap, 
			List<String> wheres, List<String> names, List<Object> values) {
		ComplexQueryParamVo vo = buildComplexQueryParamVo(clazz, key, value, descMap);
		if(vo == null) {
			return;
		}
		PredicateFactory.buildHql(vo, vo.getDescriptor(), wheres, names, values);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static<T, K> Page<T> queryData(JpaSpecificationExecutor<T> dao, Map<String, String> paramMap, Class<?> clazz, String...leftjoinName) {
		Map<String, PropertyDescriptor> descMap = ClassUtil.getClassDesc(clazz);
		PageRequest pageInfo = buildPageInfo(paramMap, descMap);
		Specification<T> queryInfo = buildQueryInfo(clazz, paramMap, descMap, leftjoinName);
		Page<T> pageData = null;
		if(pageInfo != null) {
			pageData = dao.findAll(queryInfo, pageInfo);
		}else {
			pageData = new PageImpl(dao.findAll(queryInfo));
		}
		return pageData;
		
	}
	
	@SuppressWarnings("unused")
	public static PageRequest buildPageInfo(Map<String, String> param, Map<String, PropertyDescriptor> descMap) {
		return buildPageInfo(param, descMap, true);
	}
	@SuppressWarnings("unused")
	public static PageRequest buildPageInfo(Map<String, String> param, Map<String, PropertyDescriptor> descMap, boolean deleteKey) {
		int pageNum = -1;
		int pageSize = -1;
		int pageTotalCount = -1;
		try {
			pageNum = getIntFromMap(param, PAGE_NUM_KEY);
			pageSize = getIntFromMap(param, PAGE_SIZE_KEY);
			pageTotalCount = getIntFromMap(param, PAGE_TOTAL_COUNT);
		} catch (Exception e) {
		}finally {
			if(deleteKey) {
				param.remove(PAGE_NUM_KEY);
				param.remove(PAGE_SIZE_KEY);
				param.remove(PAGE_TOTAL_COUNT);
			}
		}
		if(pageNum > 0 && pageSize > 0) {
			pageNum--;
			Sort sort = Sort.unsorted();
			if(param.containsKey(PAGE_SORT)) {
				String sortStr = param.get(PAGE_SORT);
				param.remove(PAGE_SORT);
				if(StringUtils.isNotBlank(sortStr)) {
					String[] split = sortStr.trim().split("_");
					Direction direction = Sort.DEFAULT_DIRECTION;
					if(split.length > 1) {
						if(PAGE_SORT_ASC.equalsIgnoreCase(split[1])) {
							direction = Direction.ASC;
						}else if(PAGE_SORT_DESC.equalsIgnoreCase(split[1])) {
							direction = Direction.DESC;
						}
					}
					List<String> sortKeyList = new ArrayList<>();
					String[] keySplit = split[0].split(",");
					String sortKey = null;
					for (int i = 0; i < keySplit.length; i++) {
						sortKey = keySplit[i];
						sortKey = sortKey.trim();
						if(StringUtils.isNotBlank(sortKey) && descMap.containsKey(sortKey)) {
							sortKeyList.add(sortKey);
						}
					}
					if(!sortKeyList.isEmpty()) {
						sort = Sort.by(direction, sortKeyList.toArray(new String[0]));
					}
				}
			}
			return PageRequest.of(pageNum, pageSize, sort);
		}
		return null;
	}
	public static <T> Specification<T> buildQueryInfo(final Class<?> clazz, final Map<String, String> paramMap, final Map<String, PropertyDescriptor> descMap, 
			String...leftjoinName) {
		Specification<T> specification = new Specification<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				for (int i = 0; i < leftjoinName.length; i++) {
					root.fetch(leftjoinName[i], JoinType.LEFT);
				}
				List<ComplexQueryParamVo> oringinList = new ArrayList<>();
				ComplexQueryParamVo vo = null;
				for (Map.Entry<String, String> entry : paramMap.entrySet()) {
					vo = buildComplexQueryParamVo(clazz, entry.getKey(), entry.getValue(), descMap);
					if(vo != null) {
						if(CollectionUtils.isNotEmpty(vo.getSubVoList())) {
							List<ComplexQueryParamVo> subVoList = vo.getSubVoList();
							for (ComplexQueryParamVo subVo : subVoList) {
								if(subVo != null) {
									subVo.setId(oringinList.size());
									oringinList.add(subVo);
								}
							}
						}else {
							vo.setId(oringinList.size());
							oringinList.add(vo);
						}
					}
				}
				List<ComplexQueryParamVo> destList = new ArrayList<>();
				Map<String, List<ComplexQueryParamVo>> destGroupMap = new HashMap<>();
				filterGroup(oringinList, destList, destGroupMap);
				
				List<Predicate> list = new ArrayList<>();
				Predicate predicate = null;
//				for (Map.Entry<String, String> entry : paramMap.entrySet()) {
//					predicate = buildSinglePredicate(clazz, entry.getKey(), entry.getValue(), root, builder, descMap);
//					if(predicate != null) {
//						list.add(predicate);
//					}
//				}
				for (ComplexQueryParamVo andVo : destList) {
					if(andVo == null) {
						continue;
					}
					list.add(PredicateFactory.build(root, builder, andVo, andVo.getDescriptor()));
				}
				List<Predicate> tmpList = null;
				for (List<ComplexQueryParamVo> orList : destGroupMap.values()) {
					if(orList.isEmpty()) {
						continue;
					}
					tmpList = new ArrayList<>();
					for (ComplexQueryParamVo tmpVo : orList) {
						predicate = PredicateFactory.build(root, builder, tmpVo, tmpVo.getDescriptor());
						if(predicate != null) {
							tmpList.add(predicate);
						}
					}
					if(!tmpList.isEmpty()) {
						list.add(builder.or(tmpList.toArray(new Predicate[0])));
					}
				}
				return builder.and(list.toArray(new Predicate[0]));
			}
		};
		return specification;
	}
	
	private static void filterGroup(List<ComplexQueryParamVo> oringinList, List<ComplexQueryParamVo> destList, 
			Map<String, List<ComplexQueryParamVo>> destGroupMap ) {
		List<ComplexQueryParamVo> tmpList = null;
		for (ComplexQueryParamVo source : oringinList) {
			if(source == null) {
				continue;
			}
			if(StringUtils.isBlank(source.getGroupName())) {
				destList.add(source);
			}else {
				if(destGroupMap.containsKey(source.getGroupName())) {
					tmpList = destGroupMap.get(source.getGroupName());
				}else {
					tmpList = new ArrayList<>();
					destGroupMap.put(source.getGroupName(), tmpList);
				}
				tmpList.add(source);
			}
		}
	}
	
	private static ComplexQueryParamVo buildComplexQueryParamVo(Class<?> clazz, String key, String value
			, Map<String, PropertyDescriptor> descMap) {
		if(StringUtils.isBlank(value)) {
			return null;
		}
		String[] split = key.split("_");
		if(split.length == 0) {
			return null;
		}
		String name = split[0].trim();
		
		PropertyDescriptor descriptor = findDesc(clazz, name, descMap);
		if(descriptor == null) {
			return null;
		}
		String op = ComplexQueryParamVo.OP_EQ;
		if(split.length > 1) {
			op = split[1].trim();
		}
		ComplexQueryParamVo vo = new ComplexQueryParamVo(name, value, op);
		if(split.length > 2 && StringUtils.isNotBlank(split[2])) {
			vo.setGroupName(split[2].trim());
		}
		vo.setDescriptor(descriptor);
		if(split.length > 2 && StringUtils.isNotBlank(split[2])) {
			vo.setGroupName(split[2].trim());
		}
		
 		if(Objects.equals(ComplexQueryParamVo.OP_LIKESTART_IN, op)) {
			if(StringUtils.isNotBlank(value)) {
				String[] valArr = value.split(",|;");
				String groupName = vo.getGroupName();
				if(groupName == null) {
					groupName = String.valueOf(System.currentTimeMillis());
				}
				List<ComplexQueryParamVo> subVoList = new ArrayList<>();
				ComplexQueryParamVo subVo = null;
				for (int i = 0; i < valArr.length; i++) {
					if(StringUtils.isNotBlank(valArr[i])) {
						subVo = new ComplexQueryParamVo(name, valArr[i].trim(), ComplexQueryParamVo.OP_LIKESTART);
						subVo.setDescriptor(descriptor);
						subVo.setGroupName(groupName);
						subVoList.add(subVo);
					}
				}
				vo.setSubVoList(subVoList);
				
			}else {
				return null;
			}
		}
 		if(Objects.equals(ComplexQueryParamVo.OP_LIKE_IN, op)) {
 			if(StringUtils.isNotBlank(value)) {
 				String[] valArr = value.split(",|;");
 				String groupName = vo.getGroupName();
 				if(groupName == null) {
 					groupName = String.valueOf(System.currentTimeMillis());
 				}
 				List<ComplexQueryParamVo> subVoList = new ArrayList<>();
 				ComplexQueryParamVo subVo = null;
 				for (int i = 0; i < valArr.length; i++) {
 					if(StringUtils.isNotBlank(valArr[i])) {
 						subVo = new ComplexQueryParamVo(name, valArr[i].trim(), ComplexQueryParamVo.OP_LIKE);
 						subVo.setDescriptor(descriptor);
 						subVo.setGroupName(groupName);
 						subVoList.add(subVo);
 					}
 				}
 				vo.setSubVoList(subVoList);
 				
 			}else {
 				return null;
 			}
 		}
		
		return vo;
		
	}
	@SuppressWarnings("unused")
	private static Predicate buildSinglePredicate(Class<?> clazz, String key, String value, Root<?> root, 
			CriteriaBuilder builder, Map<String, PropertyDescriptor> descMap) {
		ComplexQueryParamVo vo = buildComplexQueryParamVo(clazz, key, value, descMap);
		if(vo == null) {
			return null;
		}
		return PredicateFactory.build(root, builder, vo, vo.getDescriptor());
		
	}
	
	private static PropertyDescriptor findDesc(Class<?> clazz, String name, Map<String, PropertyDescriptor> descMap) {
		String key = clazz.getName() + ":" + name;
		if(propertyDescriptorMap.containsKey(key)) {
			return propertyDescriptorMap.get(key);
		}
		String[] split = name.split("\\.");
		if(split.length == 0) {
			return null;
		}else if(split.length == 1) {
			return descMap.get(split[0]);
		}
		PropertyDescriptor result = null;
		for (int i = 0; i < split.length; i++) {
			if(descMap == null) {
				return null;
			}
			result = descMap.get(split[i]);
			if(result == null) {
				return null;
			}else {
				if(ClassUtil.isSimpleValueType(result.getPropertyType())) {
					descMap = null;
				}else{
					descMap = ClassUtil.getClassDesc(result.getPropertyType());
				}
			}
		}
		if(result != null) {
			propertyDescriptorMap.put(key, result);
		}
		return result;
	}
	
	public static int getIntFromMap(Map<String, String> param, String key) {
		int result = -1;
		if(param.containsKey(key)) {
			result = Integer.parseInt(param.get(key));
		}
		return result;
	}
	
//	@SuppressWarnings({ "unchecked"})
//	public static<T, K> List<K> queryGroupByData(EntityManager entityManager, Map<String, String> paramMap, Class<T> clazz, QueryVo<K> queryVo) {
//		
//		Map<String, PropertyDescriptor> descMap = ClassUtil.getClassDesc(clazz);
//		String hql = "%s from " + clazz.getSimpleName() + " t where " + 
//				(ArrayUtils.isNotEmpty(queryVo.getWhere())?StringUtils.join(queryVo.getWhere(), " and "):"1=1") + 
//				" ";
//		String select = ArrayUtils.isNotEmpty(queryVo.getSelect())?StringUtils.join(queryVo.getSelect(), ","):"";
//		String groupBy = ArrayUtils.isNotEmpty(queryVo.getGroupBy())?" group by " + StringUtils.join(queryVo.getGroupBy(), ","):"";
//		
//		List<String> wheres = new ArrayList<>();
//		List<String> names = new ArrayList<>();
//		List<Object> values = new ArrayList<>();
//		buildQueryInfo(clazz, paramMap, descMap, wheres, names, values);
//		for (String where : wheres) {
//			hql += " and " + (where.trim().startsWith("(")?"":"t.") + where;
//		}
//		
//		if(queryVo.getMapClass() != null) {
//			if(queryVo.getClass().isAssignableFrom(Map.class)) {
//				select = " new java.util.HashMap(" + select + ") "; 
//			}else {
//				select = " new " + queryVo.getMapClass().getName() + "(" + select + ") ";
//			}
//			
//		}
//		select = "select " + select;
//		
//		String selectHql = String.format(hql, select) + groupBy;
//		log.debug("selectHql:[{}]", selectHql);
//		Query query = entityManager.createQuery(selectHql);
//		setParameter(query, names, values);
////		if(queryVo.getMapClass() != null) {
////			if(queryVo.getMapClass().isAssignableFrom(Map.class)) {
////				query.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
////			}else {
////				query.unwrap(QueryImpl.class).setResultTransformer(Transformers.aliasToBean(queryVo.getMapClass()));
////			}
////			
////		}
//		List<K> resultList = query.getResultList();
//		log.debug("resultList size:[{}]", resultList.size());
//		return resultList;
//	}
	@SuppressWarnings({ "unchecked"})
	public static<T, K> List<K> queryWithWrapper(EntityManager entityManager, Map<String, String> paramMap, Class<T> clazz, QueryVo<K> queryVo) {
		
		Map<String, PropertyDescriptor> descMap = ClassUtil.getClassDesc(clazz);
		String hql = "%s from " + clazz.getSimpleName() + " t where " + 
				(ArrayUtils.isNotEmpty(queryVo.getWhere())?StringUtils.join(queryVo.getWhere(), " and "):"1=1") + 
				" ";
		List<String> selectList = new ArrayList<>();
		Map<String, AbstractStandardBasicType<?>> scalarMap = new HashMap<>();
		Map<String, PropertyDescriptor> classDesc = ClassUtil.getClassDesc(queryVo.getMapClass());
		String alias = null;
		AbstractStandardBasicType<?> scalarType = null;
		for (Map.Entry<String, String> entry: queryVo.getSelectKey().entrySet()) {
			if(StringUtils.isBlank(entry.getValue())) {
				alias = entry.getKey();
			}else {
				alias = entry.getValue();
			}
			selectList.add(entry.getKey() + " as " + alias);
			if(classDesc.containsKey(alias)) {
				scalarType = QueryScalarType.getType(classDesc.get(alias).getPropertyType());
				if(scalarType != null) {
					scalarMap.put(alias, scalarType);
				}
			}
			
		}
		String select = CollectionUtils.isNotEmpty(selectList)?StringUtils.join(selectList, ","):"";
		String groupBy = ArrayUtils.isNotEmpty(queryVo.getGroupBy())?" group by " + StringUtils.join(queryVo.getGroupBy(), ","):"";
		
		List<String> wheres = new ArrayList<>();
		List<String> names = new ArrayList<>();
		List<Object> values = new ArrayList<>();
		buildQueryInfo(clazz, paramMap, descMap, wheres, names, values);
		for (String where : wheres) {
			hql += " and " + (where.trim().startsWith("(")?"":"t.") + where;
		}
		
		select = "select " + select;
		
		String selectHql = String.format(hql, select) + groupBy;
		log.debug("selectHql:[{}]", selectHql);
		Query query = entityManager.createQuery(selectHql);
		setParameter(query, names, values);
		if(queryVo.getMapClass() != null) {
			if(queryVo.getMapClass().isAssignableFrom(Map.class)) {
				query.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}else {
				query.unwrap(QueryImpl.class).setResultTransformer(new TransformerIgnoreCaseOracle(queryVo.getMapClass()));
			}
			
		}
		List<K> resultList = query.getResultList();
		log.debug("resultList size:[{}]", resultList.size());
		return resultList;
	}
	
}
