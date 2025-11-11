package org.sonic.codegen.service;

import org.apache.commons.collections4.CollectionUtils;
import org.sonic.codegen.dao.EntityDao;
import org.sonic.codegen.entity.ColumnDefinition;
import org.sonic.codegen.entity.QueryTypeConfig;
import org.sonic.codegen.entity.TableDefinition;
import org.sonicframework.context.exception.DataNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : zsljava
 * @date Date : 2020-12-15 9:50
 * @Description: TODO
 */
@Service
public class TableDefinitionService {

    @Autowired
    private EntityDao dao;

    /**
     * 查询所有记录
     *
     * @return 返回集合，没有返回空List
     */
    public List<TableDefinition> listAll() {
    	return dao.query("from TableDefinition", new String[] {}, new Object[] {});
    }

    /**
     * 查询所有记录
     *
     * @return 返回集合，没有返回空List
     */
    public List<TableDefinition> queryByDatasourceConfigId(Long datasourceConfigId) {
        return dao.query("from TableDefinition where datasourceConfigId=:datasourceConfigId", new String[] {"datasourceConfigId"}, new Object[] {datasourceConfigId});
    }

    public List<TableDefinition> queryByIds(List<Long> idList) {
        if(CollectionUtils.isEmpty(idList)){
            return new ArrayList<>();
        }
        List<TableDefinition> list = dao.findByIds(TableDefinition.class, idList, "id");
        List<Long> actualIdList = list.stream().map(t -> t.getId()).collect(Collectors.toList());
        List<ColumnDefinition> columnDefinitionList = this.dao.query("from ColumnDefinition where tableDefinitionId in (:actualIdList) order by tableDefinitionId,orderNum",
                new String[]{"actualIdList"}, new Object[]{actualIdList});
        Map<Long, List<ColumnDefinition>> columnGroupMap = columnDefinitionList.stream().collect(Collectors.groupingBy(ColumnDefinition::getTableDefinitionId));
        for (TableDefinition definition : list) {
            if(columnGroupMap.containsKey(definition.getId())){
                definition.setColumnDefinitions(columnGroupMap.get(definition.getId()));
            }else{
                definition.setColumnDefinitions(new ArrayList<>());
            }
        }
        return list;
    }

    public List<TableDefinition> queryByDatasourceConfigIdAndTableName(Long datasourceConfigId, String tableName) {
        return dao.query("from TableDefinition where datasourceConfigId=:datasourceConfigId and tableName=:tableName",
                new String[] {"datasourceConfigId", "tableName"}, new Object[] {datasourceConfigId, tableName});
    }


    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 返回记录，没有返回null
     */
    public TableDefinition getById(Long id) {
        TableDefinition tableDefinition = dao.fetch(TableDefinition.class, id);
        if(tableDefinition != null){
            List<ColumnDefinition> columns = this.dao.query("from ColumnDefinition where tableDefinitionId=:tableDefinitionId order by orderNum",
                    new String[]{"tableDefinitionId"}, new Object[]{tableDefinition.getId()});
            tableDefinition.setColumnDefinitions(columns);
        }
        return tableDefinition;
    }


    /**
     * 新增，修改所有字段
     *
     * @param tableDefinition 修改的记录
     * @return 返回影响行数
     */
    @Transactional
    public int saveOrUpdate(TableDefinition tableDefinition) {
        if(tableDefinition.getId() != null){
            this.deleteColumnDefinitionByTableId(tableDefinition.getId());
        }
    	dao.saveOrUpdate(tableDefinition, tableDefinition.getId());
        if(CollectionUtils.isNotEmpty(tableDefinition.getColumnDefinitions())){
            List<ColumnDefinition> columnDefinitionList = tableDefinition.getColumnDefinitions();
            int index = 0;
            for (ColumnDefinition columnDefinition : columnDefinitionList) {
                columnDefinition.setTableDefinitionId(tableDefinition.getId());
                columnDefinition.setOrderNum(index++);
            }
            for (ColumnDefinition columnDefinition : columnDefinitionList) {
                columnDefinition.setId(null);
                this.dao.saveOrUpdate(columnDefinition, null);
            }

        }
        return 1;
    }


    /**
     * 删除记录
     *
     * @param id 待删除的记录id
     * @return 返回影响行数
     */
    @Transactional
    public int delete(Long id) {
        if(id == null){
            throw new DataNotValidException("表定义id不能为空");
        }
        this.deleteColumnDefinitionByTableId(id);
    	int updateHql = dao.updateHql("delete from TableDefinition where id=:id", new String[]{"id"}, new Object[]{id});
        return updateHql;
    }

    @Transactional
    public int deleteBatch(List<Long> idList) {
        if(CollectionUtils.isEmpty(idList)){
            return 0;
        }
        this.deleteColumnDefinitionByTableIds(idList);
        TableDefinition tableDefinition = null;
        int updateHql = dao.updateHql("delete from TableDefinition where id in (:idList)", new String[]{"idList"}, new Object[]{idList});;
        return updateHql;
    }

    private int deleteColumnDefinitionByTableId(Long tableId){
        if(tableId == null){
            throw new DataNotValidException("表定义不能为空");
        }
        return this.dao.updateHql("delete ColumnDefinition where tableDefinitionId=:tableDefinitionId", new String[]{"tableDefinitionId"}, new Object[]{tableId});
    }

    private int deleteColumnDefinitionByTableIds(List<Long> tableIds){
        if(CollectionUtils.isEmpty(tableIds)){
            throw new DataNotValidException("表定义id不能为空");
        }
        return this.dao.updateHql("delete ColumnDefinition where tableDefinitionId in (:tableDefinitionIds)",
                new String[]{"tableDefinitionIds"}, new Object[]{tableIds});
    }

}