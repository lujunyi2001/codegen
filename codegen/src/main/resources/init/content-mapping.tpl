<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE  mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${context.packageNamePrefix}.mapper.${context.javaBeanName}Mapper">
	<resultMap id="BaseResultMap" type="${context.packageNamePrefix}.model.${context.javaBeanName}">
    #foreach($column in $columns)    
		<result column="${column.columnName}" property="${column.javaFieldName}" />
	#end
	</resultMap>  
    
	<!-- 表字段 -->
	<sql id="baseColumns">
#foreach($column in $columns)
	#if($velocityCount > 1),#end t.${column.columnName}
#end
	</sql> 
    
	<!-- 查询全部 -->
	<select id="listAll" resultMap="BaseResultMap">
		SELECT         
			<include refid="baseColumns" />
		FROM ${table.tableName} t
	</select>
    
    <!-- 根据条件查询 -->
	<select id="query" resultMap="BaseResultMap">
		SELECT         
			<include refid="baseColumns" />
		FROM ${table.tableName} t
        <where>
#foreach($column in $columns)
            <if test="${column.javaFieldName} != null">
                and t.${column.columnName}=#{${column.javaFieldName}}
            </if>
#end
		</where>
	</select>

    <!-- 根据页面条件查询 -->
	<select id="queryByOpCondition" resultMap="BaseResultMap">
		SELECT
			<include refid="baseColumns" />
		FROM ${table.tableName} t
        <where>
#foreach($column in $columns)
    #set($op = "=")
    #if($!{column.queryType}) #set($op = ${column.queryType})  #end
    #set($op = $op.replace(">", "&gt;"))
    #set($op = $op.replace("<", "&lt;"))
  #if($stringUtils.equalsIgnoreCase("between", ${column.queryType}) )
  <if test="${column.javaFieldName} != null">
                and t.${column.columnName}=#{${column.javaFieldName}}
            </if>
            <if test="${column.javaFieldName}Start != null">
                and t.${column.columnName}&gt;=#{${column.javaFieldName}Start}
            </if>
            <if test="${column.javaFieldName}End != null">
                and t.${column.columnName}&lt;=#{${column.javaFieldName}End}
            </if>
  #else
  <if test="${column.javaFieldName} != null">
                and t.${column.columnName}$op#{${column.javaFieldName}}
            </if>
  #end
#end
		</where>
	</select>

	<!-- 根据主键获取单条记录 -->
	<select id="getById" resultMap="BaseResultMap" parameterType="${pk.javaType}">
		SELECT         
			<include refid="baseColumns" />
		FROM ${table.tableName} t
		WHERE t.${pk.columnName} = #{${pk.javaFieldName}}
	</select>

	<!-- 插入全部字段 -->
	<insert id="insert" parameterType="${context.packageNamePrefix}.model.${context.javaBeanName}"
		keyProperty="${context.javaPkName}" keyColumn="${context.pkName}" useGeneratedKeys="true"
	>
		INSERT INTO ${table.tableName}
		<trim prefix="(" suffix=")" suffixOverrides=",">	 
#foreach($column in $columns) 
#if(${column.isIdentityPk}) 
			<if test="${column.javaFieldName} != null">
				${column.columnName},
			</if>
#end  
#end

#foreach($column in $columns) 
#if(!${column.isIdentityPk}) 
			${column.columnName},
#end          
#end
		</trim>
		<trim prefix="VALUES (" suffix=")" suffixOverrides=",">     
#foreach($column in $columns) 
#if(${column.isIdentityPk}) 
			<if test="${column.javaFieldName} != null">
				#{${column.javaFieldName}},        
			</if>
#end  
#end
#foreach($column in $columns) 
#if(!${column.isIdentityPk})                     
			#{${column.javaFieldName}},                   
#end          
#end
		</trim>
	</insert>

	<!-- 批量新增插入全部字段 -->
    <insert id="insertBatch" parameterType="java.util.List"
        keyProperty="${context.javaPkName}" keyColumn="${context.pkName}" useGeneratedKeys="true"
    >
        INSERT INTO ${table.tableName}
        <trim prefix="(" suffix=")" suffixOverrides=",">
#foreach($column in $columns)
    #if(!${column.isIdentityPk})
            ${column.columnName},
    #end
#end
        </trim>
        values
        <foreach collection="list" item="item" separator=",">
        (
            <trim suffixOverrides=",">
#foreach($column in $columns)
    #if(!${column.isIdentityPk})
                #{item.${column.javaFieldName}},
    #end
#end
            </trim>
        )
        </foreach>
    </insert>
    
	<!-- 插入不为NULL的字段 -->
	<insert id="insertIgnoreNull" parameterType="${context.packageNamePrefix}.model.${context.javaBeanName}"
		keyProperty="${pk.javaFieldName}" keyColumn="${context.pkName}" useGeneratedKeys="true"
	>
		INSERT INTO ${table.tableName}    
		<trim prefix="(" suffix=")" suffixOverrides=",">	 

#foreach($column in $columns) 
			<if test="${column.javaFieldName} != null">
				${column.columnName},
			</if>
#end
		</trim>
		<trim prefix="VALUES (" suffix=")" suffixOverrides=",">            
#foreach($column in $columns) 
			<if test="${column.javaFieldName} != null" >
				#{${column.javaFieldName}},                    
			</if>
#end
		</trim>
	</insert>

	<!-- 更新,更新全部字段 -->
	<update id="update" parameterType="${context.packageNamePrefix}.model.${context.javaBeanName}">
		UPDATE ${table.tableName}
		<set>		
#foreach($column in $columns) 
#if(!${column.isPk})               
			${column.columnName}=#{${column.javaFieldName}},        
#end          
#end
		</set>	
		WHERE ${pk.columnName} = #{${pk.javaFieldName}}
	</update>  
    
    
	<!-- 更新不为NULL的字段 -->
	<update id="updateIgnoreNull" parameterType="${context.packageNamePrefix}.model.${context.javaBeanName}">
		UPDATE ${table.tableName}
		<set>
#foreach($column in $columns) 
#if(!${column.isPk})  
			<if test="${column.javaFieldName} != null" >
				${column.columnName}=#{${column.javaFieldName}},                 
			</if>
#end          
#end
		</set>
		WHERE ${pk.columnName} = #{${pk.javaFieldName}}
	</update>

		
	<!-- 根据主键删除记录 -->
	<delete id="delete" parameterType="${pk.javaType}">
		DELETE FROM ${table.tableName}
		WHERE ${pk.columnName} = #{id}
	</delete>


</mapper>