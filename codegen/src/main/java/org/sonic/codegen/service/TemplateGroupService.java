package org.sonic.codegen.service;

import org.sonic.codegen.dao.EntityDao;
import org.sonic.codegen.entity.TemplateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : zsljava
 * @date Date : 2020-12-15 9:50
 * @Description: TODO
 */
@Service
public class TemplateGroupService {

    @Autowired
    private EntityDao dao;

    /**
     * 查询所有记录
     *
     * @return 返回集合，没有返回空List
     */
    public List<TemplateGroup> listAll() {
    	return dao.query("from TemplateGroup where deleted=false", new String[] {}, new Object[] {});
    }


    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 返回记录，没有返回null
     */
    public TemplateGroup getById(Long id) {
    	return dao.fetch(TemplateGroup.class, id);
    }

    /**
     * 新增，插入所有字段
     *
     * @param templateGroup 新增的记录
     * @return 返回影响行数
     */
    public int insert(TemplateGroup templateGroup) {
    	dao.saveOrUpdate(templateGroup, null);
    	return 1;
    }

    /**
     * 修改，修改所有字段
     *
     * @param templateGroup 修改的记录
     * @return 返回影响行数
     */
    public int update(TemplateGroup templateGroup) {
    	dao.saveOrUpdate(templateGroup, templateGroup.getId());
        return 1;
    }


    /**
     * 删除记录
     *
     * @param templateGroup 待删除的记录
     * @return 返回影响行数
     */
    public int delete(TemplateGroup templateGroup) {
    	int updateHql = dao.updateHql("update TemplateGroup set deleted=true where id=:id", new String[] {"id"}, new Object[] {templateGroup.getId()});
        return updateHql;
    }

}