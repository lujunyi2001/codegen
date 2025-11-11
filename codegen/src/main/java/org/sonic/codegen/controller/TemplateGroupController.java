package org.sonic.codegen.controller;

import org.sonic.codegen.entity.TemplateGroup;
import org.sonic.codegen.service.TemplateGroupService;
import org.sonicframework.core.webapi.annotation.WebApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@WebApiController
@RequestMapping("group")
public class TemplateGroupController {

    @Autowired
    private TemplateGroupService templateGroupService;

    /**
     * 查询所有记录
     * @return 
     *
     * @return 返回集合，没有返回空List
     */
    @RequestMapping("list")
    public List<TemplateGroup> listAll() {
        return templateGroupService.listAll();
    }


    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 返回记录，没有返回null
     */
    @RequestMapping("get/{id}")
    public TemplateGroup get(@PathVariable("id") Long id) {
        TemplateGroup group = templateGroupService.getById(id);
        return group;
    }

    /**
     * 新增，忽略null字段
     *
     * @param templateGroup 新增的记录
     * @return 返回影响行数
     */
    @RequestMapping("add")
    public TemplateGroup insert(@RequestBody TemplateGroup templateGroup) {
        templateGroupService.insert(templateGroup);
        return templateGroup;
    }

    /**
     * 修改，忽略null字段
     *
     * @param templateGroup 修改的记录
     * @return 返回影响行数
     */
    @RequestMapping("update")
    public void update(@RequestBody TemplateGroup templateGroup) {
        templateGroupService.update(templateGroup);
    }

    /**
     * 删除记录
     *
     * @param templateGroup 待删除的记录
     * @return 返回影响行数
     */
    @RequestMapping("del")
    public void delete(@RequestBody TemplateGroup templateGroup) {
        templateGroupService.delete(templateGroup);
    }

}