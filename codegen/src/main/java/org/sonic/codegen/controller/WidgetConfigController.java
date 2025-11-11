package org.sonic.codegen.controller;

import org.sonic.codegen.entity.WidgetConfig;
import org.sonic.codegen.service.WidgetConfigService;
import org.sonicframework.core.webapi.annotation.WebApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@WebApiController
@RequestMapping("widget")
@CrossOrigin
public class WidgetConfigController {

    @Autowired
    private WidgetConfigService widgetConfigService;

    /**
     * 查询所有记录
     * @return 
     *
     * @return 返回集合，没有返回空List
     */
    @RequestMapping("list")
    public List<WidgetConfig> listAll() {
        return widgetConfigService.listAll();
    }


    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 返回记录，没有返回null
     */
    @RequestMapping("get/{id}")
    public WidgetConfig get(@PathVariable("id") Long id) {
        WidgetConfig config = widgetConfigService.getById(id);
        return config;
    }

    /**
     * 新增，忽略null字段
     *
     * @param widgetConfig 新增的记录
     * @return 返回影响行数
     */
    @RequestMapping("save")
    public WidgetConfig save(@RequestBody WidgetConfig widgetConfig) {
        widgetConfigService.saveOrUpdate(widgetConfig);
        return widgetConfig;
    }

    /**
     * 删除记录
     *
     * @param widgetConfig 待删除的记录
     * @return 返回影响行数
     */
    @RequestMapping("del")
    public void delete(@RequestBody WidgetConfig widgetConfig) {
        widgetConfigService.delete(widgetConfig);
    }

}