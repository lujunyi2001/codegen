package org.sonic.codegen.gen.oracle;

import java.util.Arrays;
import java.util.Collections;

import org.sonic.codegen.entity.ColumnDefinition;
import org.sonic.codegen.gen.TypeFormatter;

/**
 * @author tanghc
 */
public class OracleTypeFormatter implements TypeFormatter {

    @Override
    public boolean isBit(String columnType, ColumnDefinition columnDefinition) {
        return contains(Collections.singletonList("bit"), columnType);
    }

    @Override
    public boolean isBoolean(String columnType, ColumnDefinition columnDefinition) {
        return contains(Collections.singletonList("boolean"), columnType);
    }

    @Override
    public boolean isTinyint(String columnType, ColumnDefinition columnDefinition) {
        return contains(Collections.singletonList("tinyint"), columnType);
    }

    @Override
    public boolean isSmallint(String columnType, ColumnDefinition columnDefinition) {
        return false;
    }

    @Override
    public boolean isInt(String columnType, ColumnDefinition columnDefinition) {
        boolean result = contains(Arrays.asList("int", "integer"), columnType);
        if(result) {
        	return result;
        }
        if("number".equalsIgnoreCase(columnType) && columnDefinition.getNumberScale() == 0 && columnDefinition.getNumberPrecision() <= 10) {
        	return true;
        }else {
        	return false;
        }
    }

    @Override
    public boolean isLong(String columnType, ColumnDefinition columnDefinition) {
    	boolean result = !isVarchar(columnType, columnDefinition) && contains(Collections.singletonList("long"), columnType);
    	if(result) {
        	return result;
        }
        if("number".equalsIgnoreCase(columnType) && columnDefinition.getNumberScale() == 0 && columnDefinition.getNumberPrecision() > 10) {
        	return true;
        }else {
        	return false;
        }
    }

    @Override
    public boolean isFloat(String columnType, ColumnDefinition columnDefinition) {
        return contains(Collections.singletonList("float"), columnType);
    }

    @Override
    public boolean isDouble(String columnType, ColumnDefinition columnDefinition) {
    	boolean result =  contains(Collections.singletonList("double"), columnType);
    	if(result) {
        	return result;
        }
        if("number".equalsIgnoreCase(columnType) && columnDefinition.getNumberScale() > 0) {
        	return true;
        }else {
        	return false;
        }
    }

    @Override
    public boolean isDecimal(String columnType, ColumnDefinition columnDefinition) {
        return contains(Collections.singletonList("decimal"), columnType);
    }

    @Override
    public boolean isVarchar(String columnType, ColumnDefinition columnDefinition) {
        return contains(Arrays.asList("CHAR", "VARCHAR", "VARCHAR2", "NVARCHAR2", "TEXT", "NCHAR"), columnType);
    }

    @Override
    public boolean isDatetime(String columnType, ColumnDefinition columnDefinition) {
        return contains(Arrays.asList("DATE", "TIME", "DATETIME", "TIMESTAMP"), columnType);
    }

    @Override
    public boolean isBlob(String columnType, ColumnDefinition columnDefinition) {
        return contains(Collections.singletonList("blob"), columnType);
    }
}
