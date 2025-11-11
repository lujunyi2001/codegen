package org.sonic.codegen.service;

import org.sonic.codegen.dao.EntityDao;
import org.sonic.codegen.entity.WidgetConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : zsljava
 * @date Date : 2020-12-15 9:50
 * @Description: TODO
 */
@Service
public class WidgetConfigService {

    @Autowired
    private EntityDao dao;

    /**
     * 查询所有记录
     *
     * @return 返回集合，没有返回空List
     */
    public List<WidgetConfig> listAll() {
    	return dao.query("from WidgetConfig", new String[] {}, new Object[] {});
    }


    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 返回记录，没有返回null
     */
    public WidgetConfig getById(Long id) {
    	return dao.fetch(WidgetConfig.class, id);
    }

    /**
     * 新增，修改所有字段
     *
     * @param widgetConfig 修改的记录
     * @return 返回影响行数
     */
    public int saveOrUpdate(WidgetConfig widgetConfig) {
    	dao.saveOrUpdate(widgetConfig, widgetConfig.getId());
        return 1;
    }


    /**
     * 删除记录
     *
     * @param widgetConfig 待删除的记录
     * @return 返回影响行数
     */
    public int delete(WidgetConfig widgetConfig) {
    	int updateHql = dao.delete(widgetConfig);
        return updateHql;
    }

}