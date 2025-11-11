package org.sonic.codegen.controller;

import org.sonic.codegen.entity.TemplateConfig;
import org.sonic.codegen.service.TemplateConfigService;
import org.sonicframework.core.webapi.annotation.WebApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author tanghc
 */
@WebApiController
@RequestMapping("template")
public class TemplateConfigController {

    @Autowired
    private TemplateConfigService templateConfigService;

    @RequestMapping("/add")
    public TemplateConfig add(@RequestBody TemplateConfig templateConfig) {
        templateConfigService.insert(templateConfig);
        return templateConfig;
    }

    @RequestMapping("/get/{id}")
    public TemplateConfig get(@PathVariable("id") Long id) {
        return templateConfigService.getById(id);
    }

    @RequestMapping("/list")
    public List<TemplateConfig> list(Long groupId) {
        List<TemplateConfig> templateConfigs = null;
        if(groupId == null){
            templateConfigs = templateConfigService.listAll();
        }else {
            templateConfigs = templateConfigService.listByGroupId(groupId);
        }
        return templateConfigs;
    }

    @RequestMapping("/update")
    public void update(@RequestBody TemplateConfig templateConfig) {
        templateConfigService.update(templateConfig);
    }

    @RequestMapping("/del")
    public void del(@RequestBody TemplateConfig templateConfig) {
        templateConfigService.delete(templateConfig);
    }

}
