package org.sonic.codegen.service;


import org.sonic.codegen.dao.EntityDao;
import org.sonic.codegen.entity.DatasourceConfig;
import org.sonic.codegen.gen.DbType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tanghc
 */
@Service
public class DatasourceConfigService {

    @Autowired
    private EntityDao dao;

    public DatasourceConfig getById(Long id) {
        return dao.fetch(DatasourceConfig.class, id);
    }
    public List<DatasourceConfig> getByIds(List<Long> ids) {
        return dao.findByIds(DatasourceConfig.class, ids, "id");
    }

    public List<DatasourceConfig> listAll() {
        return dao.query("from DatasourceConfig where deleted=false", new String[] {}, new Object[] {});
    }

    public void insert(DatasourceConfig datasourceConfig) {
        DbType dbType = DbType.of(datasourceConfig.getDbType());
        if (dbType != null) {
        	datasourceConfig.setDriverClass(dbType.getDriverClass());
        }
        dao.saveOrUpdate(datasourceConfig, null);
    }

    public void update(DatasourceConfig datasourceConfig) {
    	dao.saveOrUpdate(datasourceConfig, datasourceConfig.getId());
    }

    public void delete(DatasourceConfig datasourceConfig) {
    	dao.updateHql("update DatasourceConfig set deleted=true where id=:id", new String[] {"id"}, new Object[] {datasourceConfig.getId()});
    }
}
