package ${context.packageNamePrefix}.model;

#if(${table.hasDateField})
import java.util.Date;
#end
#if(${table.hasLocalDateField})
import java.time.LocalDate;
#end
#if(${table.hasLocalDateTimeField})
import java.time.LocalDateTime;
#end
#if(${table.hasBigDecimalField})
import java.math.BigDecimal;
#end

#if( "${table.comment}" != "" )
/**
 * ${table.comment}
 */
#end
public class ${context.javaBeanName} {
#foreach($column in $columns)
#if( "${column.comment}" != "" )
	/** ${column.comment} */
#end
	private ${column.javaType} ${column.javaFieldName};
#if( $stringUtils.equalsIgnoreCase("between", ${column.queryType}) )
	/** ${column.comment}开始范围 */
	private ${column.javaType} ${column.javaFieldName}Start;
    /** ${column.comment}结束范围 */
    private ${column.javaType} ${column.javaFieldName}End;
#end
#end

#foreach(${column} in ${columns})
	public ${context.javaBeanName} set${column.javaFieldNameUF}(${column.javaType} ${column.javaFieldName}) {
		this.${column.javaFieldName} = ${column.javaFieldName};
		return this;
	}

	public ${column.javaType} get${column.javaFieldNameUF}() {
		return this.${column.javaFieldName};
	}

#if( $stringUtils.equalsIgnoreCase("between", ${column.queryType}) )
    public ${context.javaBeanName} set${column.javaFieldNameUF}Start(${column.javaType} ${column.javaFieldName}Start) {
        this.${column.javaFieldName}Start = ${column.javaFieldName}Start;
        return this;
    }

    public ${column.javaType} get${column.javaFieldNameUF}Start() {
        return this.${column.javaFieldName}Start;
    }
    public ${context.javaBeanName} set${column.javaFieldNameUF}End(${column.javaType} ${column.javaFieldName}End) {
        this.${column.javaFieldName}End = ${column.javaFieldName}End;
        return this;
    }

    public ${column.javaType} get${column.javaFieldNameUF}End() {
        return this.${column.javaFieldName}End;
    }
#end
	
#end

	@Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) {return false;}
        ${context.javaBeanName} that = (${context.javaBeanName}) o;
        return java.util.Objects.equals(${pk.javaFieldName}, that.${pk.javaFieldName});
    }

	@Override
	public int hashCode() {
		return java.util.Objects.hash(${pk.javaFieldName});
	}
    
	@Override
	public String toString() {
		return "${context.javaBeanName}{" +
#foreach(${column} in ${columns})
		#if($velocityCount == 1)
		"${column.javaFieldName}=" + ${column.javaFieldName} +
		#else
		",${column.javaFieldName}='" + ${column.javaFieldName} + "'" + 
		#end
#end
		'}';
	}
	
}