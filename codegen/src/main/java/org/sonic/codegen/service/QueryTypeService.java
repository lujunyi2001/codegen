package org.sonic.codegen.service;

import org.sonic.codegen.dao.EntityDao;
import org.sonic.codegen.entity.QueryTypeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : zsljava
 * @date Date : 2020-12-15 9:50
 * @Description: TODO
 */
@Service
public class QueryTypeService {

    @Autowired
    private EntityDao dao;

    /**
     * 查询所有记录
     *
     * @return 返回集合，没有返回空List
     */
    public List<QueryTypeConfig> listAll() {
    	return dao.query("from QueryTypeConfig", new String[] {}, new Object[] {});
    }


    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 返回记录，没有返回null
     */
    public QueryTypeConfig getById(Long id) {
    	return dao.fetch(QueryTypeConfig.class, id);
    }

    /**
     * 新增，修改所有字段
     *
     * @param queryTypeConfig 修改的记录
     * @return 返回影响行数
     */
    public int saveOrUpdate(QueryTypeConfig queryTypeConfig) {
    	dao.saveOrUpdate(queryTypeConfig, queryTypeConfig.getId());
        return 1;
    }


    /**
     * 删除记录
     *
     * @param queryTypeConfig 待删除的记录
     * @return 返回影响行数
     */
    public int delete(QueryTypeConfig queryTypeConfig) {
    	int updateHql = dao.delete(queryTypeConfig);
        return updateHql;
    }

}