package org.sonic.codegen.service;

import org.sonic.codegen.dao.EntityDao;
import org.sonic.codegen.entity.TemplateConfig;
import org.sonicframework.context.exception.DataNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author tanghc
 */
@Service
public class TemplateConfigService {

	@Autowired
    private EntityDao dao;

    public List<TemplateConfig> listTemplate(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        return dao.query("from DatasourceConfig where deleted=false and id in (:idList)", new String[] {"idList"}, new Object[] {idList});
    }

    public TemplateConfig getById(Long id) {
    	return dao.fetch(TemplateConfig.class, id);
    }

    public List<TemplateConfig> listAll() {
    	return dao.query("from TemplateConfig where deleted=false", new String[] {}, new Object[] {});
    }

    public void insert(TemplateConfig templateConfig) {
    	checkExist(templateConfig);
    	templateConfig.setDeleted(false);
        dao.saveOrUpdate(templateConfig, null);
    }

    public void update(TemplateConfig templateConfig) {
    	checkExist(templateConfig);
        dao.saveOrUpdate(templateConfig, templateConfig.getId());
    }
    
    private void checkExist(TemplateConfig templateConfig) {
    	Long id = templateConfig.getId();
    	String hql = "from TemplateConfig where deleted=false and name=:name";
    	List<TemplateConfig> list = dao.query(hql, new String[] {"name"}, new Object[] {templateConfig.getName()});
    	if(id == null && !list.isEmpty() || list.stream().filter(t->!Objects.equals(id, t.getId())).count() > 0) {
    		throw new DataNotValidException("模板名称已存在");
    	}
    }

    public void delete(TemplateConfig templateConfig) {
    	dao.updateHql("update TemplateConfig set deleted=true where id=:id", new String[] {"id"}, new Object[] {templateConfig.getId()});
    }

    public List<TemplateConfig> listByGroupId(Long groupId) {
    	String hql = "from TemplateConfig where deleted=false and groupId=:groupId";
    	List<TemplateConfig> list = dao.query(hql, new String[] {"groupId"}, new Object[] {groupId});
        return list;
    }
}
