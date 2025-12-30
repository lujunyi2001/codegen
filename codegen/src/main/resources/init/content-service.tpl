package ${context.packageNamePrefix}.service;

import ${context.packageNamePrefix}.model.${context.javaBeanName};
import ${context.packageNamePrefix}.mapper.${context.javaBeanName}Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ${context.javaBeanName}Service {

	@Autowired
	private ${context.javaBeanName}Mapper ${context.javaBeanNameLF}Mapper;

	/**
	 * 查询所有记录
	 *
	 * @return 返回集合，没有返回空List
	 */
	public List<${context.javaBeanName}> listAll() {
		return ${context.javaBeanNameLF}Mapper.listAll();
	}
    
    /**
	 * 根据条件查询记录
	 *
	 * @param ${context.javaBeanNameLF} 新增的记录
	 * @return 返回集合，没有返回空List
	 */
	public List<${context.javaBeanName}> query(${context.javaBeanName} ${context.javaBeanNameLF}) {
		return ${context.javaBeanNameLF}Mapper.query(${context.javaBeanNameLF});
	}


	/**
	 * 根据主键查询
	 *
	 * @param id 主键
	 * @return 返回记录，没有返回null
	 */
	public ${context.javaBeanName} getById(${pk.javaType} ${pk.javaFieldName}) {
		return ${context.javaBeanNameLF}Mapper.getById(${pk.javaFieldName});
	}
	
	/**
	 * 新增，插入所有字段
	 *
	 * @param ${context.javaBeanNameLF} 新增的记录
	 * @return 返回影响行数
	 */
	public int insert(${context.javaBeanName} ${context.javaBeanNameLF}) {
		return ${context.javaBeanNameLF}Mapper.insert(${context.javaBeanNameLF});
	}
	
	/**
	 * 新增，忽略null字段
	 *
	 * @param ${context.javaBeanNameLF} 新增的记录
	 * @return 返回影响行数
	 */
	public int insertIgnoreNull(${context.javaBeanName} ${context.javaBeanNameLF}) {
		return ${context.javaBeanNameLF}Mapper.insertIgnoreNull(${context.javaBeanNameLF});
	}
	
	/**
	 * 修改，修改所有字段
	 *
	 * @param ${context.javaBeanNameLF} 修改的记录
	 * @return 返回影响行数
	 */
	public int update(${context.javaBeanName} ${context.javaBeanNameLF}) {
		return ${context.javaBeanNameLF}Mapper.update(${context.javaBeanNameLF});
	}
	
	/**
	 * 修改，忽略null字段
	 *
	 * @param ${context.javaBeanNameLF} 修改的记录
	 * @return 返回影响行数
	 */
	public int updateIgnoreNull(${context.javaBeanName} ${context.javaBeanNameLF}) {
		return ${context.javaBeanNameLF}Mapper.updateIgnoreNull(${context.javaBeanNameLF});
	}
	
	/**
	 * 删除记录
	 *
	 * @param ${pk.javaFieldName} 待删除的记录id
	 * @return 返回影响行数
	 */
	public int delete(${pk.javaType} ${pk.javaFieldName}) {
		return ${context.javaBeanNameLF}Mapper.delete(${pk.javaFieldName});
	}
	
}