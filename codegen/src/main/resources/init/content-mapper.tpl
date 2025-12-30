package ${context.packageNamePrefix}.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ${context.packageNamePrefix}.model.${context.javaBeanName};

@Mapper
public interface ${context.javaBeanName}Mapper {

	/**
	 * 查询所有记录
	 *
	 * @return 返回集合，没有返回空List
	 */
	List<${context.javaBeanName}> listAll();

	/**
	 * 根据条件查询
	 *
	 * @param ${context.javaBeanNameLF} 查询条件
	 * @return 返回集合，没有返回空List
	 */
	List<${context.javaBeanName}> query(${context.javaBeanName} ${context.javaBeanNameLF});

	/**
     * 根据页面条件查询
     *
     * @param ${context.javaBeanNameLF} 查询条件
     * @return 返回集合，没有返回空List
     */
    List<${context.javaBeanName}> queryByOpCondition(${context.javaBeanName} ${context.javaBeanNameLF});

	/**
	 * 根据主键查询
	 *
	 * @param id 主键
	 * @return 返回记录，没有返回null
	 */
	${context.javaBeanName} getById(${pk.javaType} ${pk.javaFieldName});
	
	/**
	 * 新增，插入所有字段
	 *
	 * @param ${context.javaBeanNameLF} 新增的记录
	 * @return 返回影响行数
	 */
	int insert(${context.javaBeanName} ${context.javaBeanNameLF});

/**
	 * 批量新增，插入所有字段
	 *
	 * @param list 新增的记录
	 * @return 返回影响行数
	 */
	int insertBatch(List<${context.javaBeanName}> list);

	/**
	 * 新增，忽略null字段
	 *
	 * @param ${context.javaBeanNameLF} 新增的记录
	 * @return 返回影响行数
	 */
	int insertIgnoreNull(${context.javaBeanName} ${context.javaBeanNameLF});
	
	/**
	 * 修改，修改所有字段
	 *
	 * @param ${context.javaBeanNameLF} 修改的记录
	 * @return 返回影响行数
	 */
	int update(${context.javaBeanName} ${context.javaBeanNameLF});
	
	/**
	 * 修改，忽略null字段
	 *
	 * @param ${context.javaBeanNameLF} 修改的记录
	 * @return 返回影响行数
	 */
	int updateIgnoreNull(${context.javaBeanName} ${context.javaBeanNameLF});
	
	/**
	 * 删除记录
	 *
	 * @param ${pk.javaFieldName} 待删除的记录id
	 * @return 返回影响行数
	 */
	int delete(@Param("id") ${pk.javaType} ${pk.javaFieldName});
	
}