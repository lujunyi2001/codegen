package org.sonic.codegen.util;

import org.hibernate.HibernateException;
import org.hibernate.property.access.internal.PropertyAccessStrategyBasicImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyChainedImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyFieldImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyMapImpl;
import org.hibernate.property.access.spi.Setter;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;
import org.sonicframework.utils.ClassUtil;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TransformerIgnoreCaseOracle extends AliasedTupleSubsetResultTransformer {

	private static final long serialVersionUID = 4507325697460882442L;
	private final Class<?> resultClass;
    private boolean isInitialized;
    private String[] aliases;
    private Setter[] setters;
	private Map<String, String> resultClassMap;
    
    private static Map<Class<?>, Map<String, String>> descMap = new ConcurrentHashMap<>();

    public TransformerIgnoreCaseOracle(Class<?> resultClass) {
        if ( resultClass == null ) {
            throw new IllegalArgumentException( "resultClass cannot be null" );
        }
        isInitialized = false;
        this.resultClass = resultClass;
        this.resultClassMap = initMap(resultClass);
    }
    
    private Map<String, String> initMap(Class<?> resultClass) {
    	if(descMap.containsKey(resultClass)) {
    		return descMap.get(resultClass);
    	}
    	synchronized (TransformerIgnoreCaseOracle.class) {
    		if(descMap.containsKey(resultClass)) {
        		return descMap.get(resultClass);
        	}
    		Map<String, String> map = new HashMap<String, String>();
    		Map<String, PropertyDescriptor> classDesc = ClassUtil.getClassDesc(resultClass);
    		for (String name : classDesc.keySet()) {
    			map.put(name.toUpperCase(), name);
			}
    		descMap.put(resultClass, map);
    		return map;
		}
    	
    }

    @Override
    public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
        return false;
    }

    /**
     *
     * @param tuple 查询结果一行数据
     * @param aliases 查询结果的字段名称
     * @return
     */
    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Object result;
        // oracle返回大写字段，如果有下划线，转为驼峰命名
        for ( int i = 0; i < aliases.length; i++ ) {
            aliases[i] = convert(aliases[i]);
        }

        try {
            if ( ! isInitialized ) {
                initialize( aliases );
            }
            else {
                check( aliases );
            }

            result = resultClass.newInstance();

            for ( int i = 0; i < aliases.length; i++ ) {
                if ( setters[i] != null && tuple[i] != null) {
                    setters[i].set( result, tuple[i], null );
                }
            }
        }
        catch ( InstantiationException e ) {
            throw new HibernateException( "Could not instantiate resultclass: " + resultClass.getName() );
        }
        catch ( IllegalAccessException e ) {
            throw new HibernateException( "Could not instantiate resultclass: " + resultClass.getName() );
        }

        return result;
    }

    private void initialize(String[] aliases) {
        PropertyAccessStrategyChainedImpl propertyAccessStrategy = new PropertyAccessStrategyChainedImpl(
                PropertyAccessStrategyBasicImpl.INSTANCE,
                PropertyAccessStrategyFieldImpl.INSTANCE,
                PropertyAccessStrategyMapImpl.INSTANCE
        );
        this.aliases = new String[ aliases.length ];
        setters = new Setter[ aliases.length ];
        for ( int i = 0; i < aliases.length; i++ ) {
            String alias = aliases[ i ];
            if ( alias != null ) {
                this.aliases[ i ] = convert(alias);
                setters[ i ] = propertyAccessStrategy.buildPropertyAccess( resultClass, alias ).getSetter();
            }
        }
        isInitialized = true;
    }

    private void check(String[] aliases) {
        if ( ! Arrays.equals( aliases, this.aliases ) ) {
            throw new IllegalStateException(
                    "aliases are different from what is cached; aliases=" + Arrays.asList( aliases ) +
                            " cached=" + Arrays.asList( this.aliases ) );
        }
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        TransformerIgnoreCaseOracle that = ( TransformerIgnoreCaseOracle ) o;

        if ( ! resultClass.equals( that.resultClass ) ) {
            return false;
        }
        if ( ! Arrays.equals( aliases, that.aliases ) ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = resultClass.hashCode();
        result = 31 * result + ( aliases != null ? Arrays.hashCode( aliases ) : 0 );
        return result;
    }


    public String convert(String param){
        if (param==null||"".equals(param.trim())){
            return null;
        }
        param=param.toUpperCase();
        return this.resultClassMap.get(param);
    }
}