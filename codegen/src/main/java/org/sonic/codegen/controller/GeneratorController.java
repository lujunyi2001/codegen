package org.sonic.codegen.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.sonic.codegen.dto.GeneratorParam;
import org.sonic.codegen.entity.DatasourceConfig;
import org.sonic.codegen.entity.TableDefinition;
import org.sonic.codegen.gen.CodeFile;
import org.sonic.codegen.gen.GeneratorConfig;
import org.sonic.codegen.service.DatasourceConfigService;
import org.sonic.codegen.service.GeneratorService;
import org.sonic.codegen.service.TableDefinitionService;
import org.sonicframework.context.exception.DataCheckException;
import org.sonicframework.context.exception.DataNotValidException;
import org.sonicframework.core.webapi.annotation.WebApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@WebApiController
@RequestMapping("generate")
public class GeneratorController {

    @Autowired
    private DatasourceConfigService datasourceConfigService;

    @Autowired
    private GeneratorService generatorService;
    @Autowired
    private TableDefinitionService tableDefinitionService;

    /**
     * 生成代码
     *
     * @param generatorParam 生成参数
     * @return 
     * @return 返回代码内容
     */
    @RequestMapping("/code")
    public List<CodeFile> code(@RequestBody GeneratorParam generatorParam) {
        Long datasourceConfigId = generatorParam.getDatasourceConfigId();
        DatasourceConfig datasourceConfig = datasourceConfigService.getById(datasourceConfigId);
        GeneratorConfig generatorConfig = GeneratorConfig.build(datasourceConfig);
        return generatorService.generate(generatorParam, generatorConfig);
    }

    @RequestMapping("/codeFromDefinition")
    public List<CodeFile> codeFromDefinition(@RequestBody GeneratorParam generatorParam) {
        if(CollectionUtils.isEmpty(generatorParam.getTableDefinitionIds())){
            throw new DataNotValidException("没有传入表定义id");
        }
        List<TableDefinition> tableDefinitions = tableDefinitionService.queryByIds(generatorParam.getTableDefinitionIds());
        Set<Long> datasourceConfigIdSet = tableDefinitions.stream().map(t -> t.getDatasourceConfigId()).collect(Collectors.toSet());
        Map<Long, DatasourceConfig> datasourceConfigMap = datasourceConfigService.getByIds(new ArrayList<>(datasourceConfigIdSet))
                .stream().collect(Collectors.toMap(DatasourceConfig::getId, t->t, (t1, t2)->t2));
        GeneratorConfig generatorConfig = null;
        List<CodeFile> result = new ArrayList<>();
        for (TableDefinition tableDefinition : tableDefinitions) {
            if(!datasourceConfigMap.containsKey(tableDefinition.getDatasourceConfigId())){
                throw new DataCheckException("表" + tableDefinition.getTableName() + "没有找到数据源");
            }
            generatorConfig = GeneratorConfig.build(datasourceConfigMap.get(tableDefinition.getDatasourceConfigId()));
            result.addAll(generatorService.generateFromTableDefinition(generatorParam, generatorConfig, tableDefinition));
        }
        return result;
    }

    @RequestMapping("/generateTableDefinition")
    public List<TableDefinition> generateTableDefinition(@Validated @RequestBody GeneratorParam generatorParam) {
        Long datasourceConfigId = generatorParam.getDatasourceConfigId();
        DatasourceConfig datasourceConfig = datasourceConfigService.getById(datasourceConfigId);
        GeneratorConfig generatorConfig = GeneratorConfig.build(datasourceConfig);
        return this.generatorService.generateTableDefinition(generatorParam, generatorConfig);
    }

}
