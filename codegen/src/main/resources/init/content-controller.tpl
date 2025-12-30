package ${context.packageNamePrefix}.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ${context.packageNamePrefix}.model.${context.javaBeanName};
import ${context.packageNamePrefix}.service.${context.javaBeanName}Service;

@RestController
@RequestMapping("${context.javaBeanNameLF}")
public class ${context.javaBeanName}Controller {

    @Autowired
    private ${context.javaBeanName}Service ${context.javaBeanNameLF}Service;

    /**
     * 查询所有记录
     *
     * @return 返回集合，没有返回空List
     */
    @RequestMapping("list")
    public List<${context.javaBeanName}> listAll() {
        return ${context.javaBeanNameLF}Service.listAll();
    }
    
    /**
     * 根据条件查询
     *
     * @return 返回集合，没有返回空List
     */
    @RequestMapping("query")
    public List<${context.javaBeanName}> query(${context.javaBeanName} ${context.javaBeanNameLF}) {
        return ${context.javaBeanNameLF}Service.query(${context.javaBeanNameLF});
    }


    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 返回记录，没有返回null
     */
    @GetMapping("/{id}")
    public ${context.javaBeanName} getById(@PathVariable("id") ${pk.javaType} ${pk.javaFieldName}) {
        return ${context.javaBeanNameLF}Service.getById(${pk.javaFieldName});
    }    
     
    /**
     * 新增，忽略null字段
     *
     * @param ${context.javaBeanNameLF} 新增的记录
     * @return 返回影响行数
     */
    @PostMapping
    public int insert(@RequestBody ${context.javaBeanName} ${context.javaBeanNameLF}) {
        return ${context.javaBeanNameLF}Service.insertIgnoreNull(${context.javaBeanNameLF});
    }    
      
    /**
     * 修改，忽略null字段
     *
     * @param ${context.javaBeanNameLF} 修改的记录
     * @return 返回影响行数
     */
    @PutMapping
    public int update(@RequestBody ${context.javaBeanName} ${context.javaBeanNameLF}) {
        return ${context.javaBeanNameLF}Service.updateIgnoreNull(${context.javaBeanNameLF});
    }
    
    /**
     * 删除记录
     *
     * @param ${pk.javaFieldName} 待删除的记录
     * @return 返回影响行数
     */
    @DeleteMapping("/{id}")
    public int delete(@PathVariable("id") ${pk.javaType} ${pk.javaFieldName}) {
        return ${context.javaBeanNameLF}Service.delete(${pk.javaFieldName});
    }
    
}