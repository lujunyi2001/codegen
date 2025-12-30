package org.sonic.codegen.service;

import org.apache.commons.io.IOUtils;
import org.sonic.codegen.config.InitConfig;
import org.sonic.codegen.dto.InitTemplateDto;
import org.sonic.codegen.entity.QueryTypeConfig;
import org.sonic.codegen.entity.TemplateConfig;
import org.sonic.codegen.entity.TemplateGroup;
import org.sonicframework.context.exception.FileCheckException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InitCommandLineRunner implements CommandLineRunner {
    @Autowired
    private InitConfig initConfig;
    @Autowired
    private TemplateGroupService templateGroupService;
    @Autowired
    private TemplateConfigService templateConfigService;
    @Autowired
    private QueryTypeService queryTypeService;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        initTemplate();
        initQueryType();
    }

    private void initQueryType(){
        List<QueryTypeConfig> queryTypeList = queryTypeService.listAll();
        if(!queryTypeList.isEmpty()){
            return ;
        }
        queryTypeService.saveOrUpdate(buildQueryTypeConfig("=", "等于"));
        queryTypeService.saveOrUpdate(buildQueryTypeConfig(">", "大于"));
        queryTypeService.saveOrUpdate(buildQueryTypeConfig("<", "小于"));
        queryTypeService.saveOrUpdate(buildQueryTypeConfig(">=", "大于等于"));
        queryTypeService.saveOrUpdate(buildQueryTypeConfig("<=", "小于等于"));
        queryTypeService.saveOrUpdate(buildQueryTypeConfig("between", "之间"));
    }

    private QueryTypeConfig buildQueryTypeConfig(String code, String desc){
        QueryTypeConfig config = new QueryTypeConfig();
        config.setCode(code);
        config.setName(desc);
        return config;
    }

    private void initTemplate(){
        List<TemplateGroup> groupList = templateGroupService.listAll();
        if(!groupList.isEmpty()){
            return ;
        }
        Map<String, List<InitTemplateDto>> initTemplateMap = initConfig.getInitTemplateMap();
        for (Map.Entry<String, List<InitTemplateDto>> entry : initTemplateMap.entrySet()) {
            initTemplateGroupData(entry.getKey(), entry.getValue());
        }
    }

    private void initTemplateGroupData(String groupName, List<InitTemplateDto> initTempList){
        TemplateGroup group = new TemplateGroup();
        group.setGroupName(groupName);
        List<TemplateConfig> templateList = initTempList.stream().map(t -> {
            TemplateConfig c = new TemplateConfig();
            c.setGroupName(groupName);
            c.setName(t.getName());
            c.setFileName(t.getFileName());
            c.setContent(readFile(t.getContentFile()));
            return c;
        }).collect(Collectors.toList());
        this.templateGroupService.insert(group);
        for (TemplateConfig templateConfig : templateList) {
            templateConfig.setGroupId(group.getId());
            templateConfigService.insert(templateConfig);
        }
    }
    private String readFile(String path) {
        ClassPathResource resource = new ClassPathResource(path);
        if(!resource.exists()){
            throw new FileCheckException("文件" + path + "不存在");
        }

        try(InputStream input = resource.getInputStream()){
            return IOUtils.toString(input, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new FileCheckException("读取文件" + path + "错误", e);
        }
    }
}
