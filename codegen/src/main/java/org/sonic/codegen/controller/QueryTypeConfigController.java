package org.sonic.codegen.controller;

import org.sonic.codegen.entity.QueryTypeConfig;
import org.sonic.codegen.entity.TemplateGroup;
import org.sonic.codegen.service.QueryTypeService;
import org.sonic.codegen.service.TemplateGroupService;
import org.sonicframework.core.webapi.annotation.WebApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@WebApiController
@RequestMapping("queryType")
public class QueryTypeConfigController {

    @Autowired
    private QueryTypeService queryTypeService;

    /**
     * 查询所有记录
     * @return 
     *
     * @return 返回集合，没有返回空List
     */
    @RequestMapping("list")
    public List<QueryTypeConfig> listAll() {
        return queryTypeService.listAll();
    }


    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 返回记录，没有返回null
     */
    @RequestMapping("get/{id}")
    public QueryTypeConfig get(@PathVariable("id") Long id) {
        QueryTypeConfig config = queryTypeService.getById(id);
        return config;
    }

    /**
     * 新增，忽略null字段
     *
     * @param queryTypeConfig 新增的记录
     * @return 返回影响行数
     */
    @RequestMapping("save")
    public QueryTypeConfig save(@RequestBody QueryTypeConfig queryTypeConfig) {
        queryTypeService.saveOrUpdate(queryTypeConfig);
        return queryTypeConfig;
    }

    /**
     * 删除记录
     *
     * @param queryTypeConfig 待删除的记录
     * @return 返回影响行数
     */
    @RequestMapping("del")
    public void delete(@RequestBody QueryTypeConfig queryTypeConfig) {
        queryTypeService.delete(queryTypeConfig);
    }

}