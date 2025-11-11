package org.sonic.codegen.gen.mysql;

import java.util.Arrays;
import java.util.Collections;

import org.sonic.codegen.entity.ColumnDefinition;
import org.sonic.codegen.gen.TypeFormatter;

/**
 * @author tanghc
 */
public class MySqlTypeFormatter implements TypeFormatter {

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
        return contains(Collections.singletonList("smallint"), columnType);
    }

    @Override
    public boolean isInt(String columnType, ColumnDefinition columnDefinition) {
        return !isLong(columnType, columnDefinition) && contains(Arrays.asList("int", "integer"), columnType);
    }

    @Override
    public boolean isLong(String columnType, ColumnDefinition columnDefinition) {
        return !isVarchar(columnType, columnDefinition) && contains(Collections.singletonList("bigint"), columnType);
    }

    @Override
    public boolean isFloat(String columnType, ColumnDefinition columnDefinition) {
        return contains(Collections.singletonList("float"), columnType);
    }

    @Override
    public boolean isDouble(String columnType, ColumnDefinition columnDefinition) {
        return contains(Collections.singletonList("double"), columnType);
    }

    @Override
    public boolean isDecimal(String columnType, ColumnDefinition columnDefinition) {
        return contains(Collections.singletonList("decimal"), columnType);
    }

    @Override
    public boolean isVarchar(String columnType, ColumnDefinition columnDefinition) {
        return contains(Arrays.asList("CHAR", "VARCHAR", "TEXT"), columnType);
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
