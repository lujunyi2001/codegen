package org.sonic.codegen.controller;

import org.apache.commons.collections4.CollectionUtils;
import org.sonic.codegen.dto.GeneratorParam;
import org.sonic.codegen.entity.DatasourceConfig;
import org.sonic.codegen.entity.TableDefinition;
import org.sonic.codegen.gen.*;
import org.sonic.codegen.service.DatasourceConfigService;
import org.sonic.codegen.service.TableDefinitionService;
import org.sonicframework.context.exception.DataCheckException;
import org.sonicframework.context.exception.DataNotValidException;
import org.sonicframework.core.webapi.annotation.WebApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebApiController
@RequestMapping("definition")
public class DefinitionController {

    @Autowired
    private TableDefinitionService definitionService;

    @RequestMapping("/save")
    public void save(@Validated @RequestBody TableDefinition tableDefinition) {
        definitionService.saveOrUpdate(tableDefinition);
    }

    @RequestMapping("/query")
    public List<TableDefinition> query(@Validated @RequestBody TableDefinition tableDefinition) {
        List<TableDefinition> list = this.definitionService.queryByDatasourceConfigId(tableDefinition.getDatasourceConfigId());
        return list;
    }

    @RequestMapping("/del")
    public void del(@Validated TableDefinition tableDefinition) {
        if(tableDefinition.getId() == null){
            throw new DataNotValidException("id不能为空");
        }
        this.definitionService.delete(tableDefinition.getId());
    }
    @RequestMapping("/delBatch")
    public void del(@RequestBody GeneratorParam generatorParam) {
        if(CollectionUtils.isEmpty(generatorParam.getTableDefinitionIds())){
            throw new DataNotValidException("id不能为空");
        }
        this.definitionService.deleteBatch(generatorParam.getTableDefinitionIds());
    }

    @RequestMapping("/table/{id}")
    public TableDefinition listTable(@PathVariable("id") long id) {
        TableDefinition tableDefinition = definitionService.getById(id);
        return tableDefinition;
    }


}
