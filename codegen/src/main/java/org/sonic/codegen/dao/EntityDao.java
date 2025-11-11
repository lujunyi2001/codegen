package org.sonic.codegen.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonic.codegen.util.ComplexQueryUtil;
import org.sonicframework.context.exception.DataCheckException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lujunyi
 */
@Transactional
@Service
public class EntityDao {

	@Autowired
	protected EntityManager entityManager;
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	public EntityDao() {
	}

	public <T, K>T saveOrUpdate(T entity, K id) {
		if(id == null) {
			this.entityManager.persist(entity);
		}else {
			this.entityManager.merge(entity);
		}
		entityManager.flush();
		return entity;
	}
	
	public <T, K> T fetch(Class<T> entityClass, K primaryKey) {

		if (primaryKey == null) {
			return null;
		}
		T entity = this.entityManager.find(entityClass, primaryKey);
		if (entity != null) {
			this.entityManager.detach(entity);
		}
		return entity;
	}
	
	public <T>Page<T> queryData(Map<String, String> paramMap, Class<T> clazz, String...leftjoinName){
		return ComplexQueryUtil.queryData(entityManager, paramMap, clazz, leftjoinName);
	}
	
	public <T>List<T> query(String hql, String[] params, Object[] values){
		return query(hql, params, values, -1, -1);
	}
	@SuppressWarnings("unchecked")
	public <T>List<T> query(String hql, String[] params, Object[] values, int startIndex, int maxResult){
		if(params.length != values.length) {
			throw new DataCheckException("hql参数名和值数量不一致");
		}
		Query query = this.entityManager.createQuery(hql);
		log.trace("execute hql:[{}]", hql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(params[i], values[i]);
			log.trace("setParameter[{}]->[{}]", params[i], values[i]);
		}
		if(startIndex >= 0 && maxResult > 0) {
			query.setFirstResult(startIndex).setMaxResults(maxResult);
		}
		return query.getResultList();
	}
	@SuppressWarnings("unchecked")
	public <T, K>List<T> findByIds(Class<T> clazz, List<K> idList, String idField){
		if(CollectionUtils.isEmpty(idList)) {
			return new ArrayList<>();
		}
		String hql = "from " + clazz.getSimpleName() + " where " + idField + " in (:ids)";
		Query query = this.entityManager.createQuery(hql);
		log.trace("findByIds execute hql:[{}]", hql);
		query.setParameter("ids", idList);
		log.trace("findByIds setParameter[{}]->[{}]", idField, idList);
		return query.getResultList();
	}
	public int updateHql(String hql, String[] params, Object[] values){
		if(params.length != values.length) {
			throw new DataCheckException("hql参数名和值数量不一致");
		}
		Query query = this.entityManager.createQuery(hql);
		log.trace("execute hql:[{}]", hql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(params[i], values[i]);
			log.trace("setParameter[{}]->[{}]", params[i], values[i]);
		}
		return query.executeUpdate();
	}
	public int updateSql(String sql, String[] params, Object[] values){
		if(params.length != values.length) {
			throw new DataCheckException("hql参数名和值数量不一致");
		}
		Query query = this.entityManager.createNativeQuery(sql);
		log.trace("execute sql:[{}]", sql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(params[i], values[i]);
			log.trace("setParameter[{}]->[{}]", params[i], values[i]);
		}
		return query.executeUpdate();
	}
	
	public int delete(Object entity){
		if(entity == null) {
			throw new DataCheckException("删除参数不能为空");
		}
		this.entityManager.remove(entity);
		log.trace("delete :[{}]", entity);
		return 1;
	}

}
