package org.sonic.codegen.controller;

import org.sonic.codegen.entity.DatasourceConfig;
import org.sonic.codegen.gen.DBConnect;
import org.sonic.codegen.gen.DbType;
import org.sonic.codegen.gen.GeneratorConfig;
import org.sonic.codegen.gen.SQLService;
import org.sonic.codegen.gen.SQLServiceFactory;
import org.sonic.codegen.entity.TableDefinition;
import org.sonic.codegen.service.DatasourceConfigService;
import org.sonicframework.context.exception.DataCheckException;
import org.sonicframework.core.webapi.annotation.WebApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebApiController
@RequestMapping("datasource")
public class DatasourceConfigController {

    @Autowired
    private DatasourceConfigService datasourceConfigService;

    @RequestMapping("/add")
    public void add(@RequestBody DatasourceConfig datasourceConfig) {
        datasourceConfigService.insert(datasourceConfig);
    }

    @RequestMapping("/list")
    public List<DatasourceConfig> list() {
        List<DatasourceConfig> datasourceConfigList = datasourceConfigService.listAll();
        return datasourceConfigList;
    }

    @RequestMapping("/update")
    public void update(@RequestBody DatasourceConfig datasourceConfig) {
        datasourceConfigService.update(datasourceConfig);
    }

    @RequestMapping("/del")
    public void del(@RequestBody DatasourceConfig datasourceConfig) {
        datasourceConfigService.delete(datasourceConfig);
    }

    @RequestMapping("/table/{id}")
    public List<TableDefinition> listTable(@PathVariable("id") long id) {
        DatasourceConfig dataSourceConfig = datasourceConfigService.getById(id);
        GeneratorConfig generatorConfig = GeneratorConfig.build(dataSourceConfig);
        SQLService service = SQLServiceFactory.build(generatorConfig);
        List<TableDefinition> list = service.getTableSelector(generatorConfig).getSimpleTableDefinitions();
        return list;
    }


    @RequestMapping("/test")
    public void test(@RequestBody DatasourceConfig datasourceConfig) {
        String error = DBConnect.testConnection(GeneratorConfig.build(datasourceConfig));
        if (error != null) {
            throw new DataCheckException(error);
        }
    }

    @RequestMapping("/dbtype")
    public List<DbTypeShow> dbType(@RequestBody DatasourceConfig datasourceConfig) {
        List<DbTypeShow> dbTypeShowList = Stream.of(DbType.values())
                .map(dbType -> new DbTypeShow(dbType.getDisplayName(), dbType.getType()))
                .collect(Collectors.toList());
        return dbTypeShowList;
    }

    @SuppressWarnings("unused")
	private static class DbTypeShow {
        private String label;
        private Integer dbType;

        public DbTypeShow(String label, Integer dbType) {
            this.label = label;
            this.dbType = dbType;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public Integer getDbType() {
            return dbType;
        }

        public void setDbType(Integer dbType) {
            this.dbType = dbType;
        }
    }


}
