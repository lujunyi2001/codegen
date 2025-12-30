package org.sonic.codegen.config;

import org.sonic.codegen.dto.InitTemplateDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties("init")
public class InitConfig {

    private Map<String, List<InitTemplateDto>> initTemplateMap = new HashMap<>();

    public Map<String, List<InitTemplateDto>> getInitTemplateMap() {
        return initTemplateMap;
    }

    public void setInitTemplateMap(Map<String, List<InitTemplateDto>> initTemplateMap) {
        this.initTemplateMap = initTemplateMap;
    }
}
