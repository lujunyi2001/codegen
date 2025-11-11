package org.sonic.codegen.gen;

import org.apache.commons.lang.StringUtils;
import org.sonic.codegen.entity.ColumnDefinition;

import java.util.List;

/**
 * 将各数据库类型格式化成统一的类型
 * @see TypeEnum
 * @author tanghc
 */
public interface TypeFormatter {

    default String format(String columnType, ColumnDefinition columnDefinition) {
        if (isBit(columnType, columnDefinition)) {
            return TypeEnum.BIT.getType();
        }
        if (isBoolean(columnType, columnDefinition)) {
            return TypeEnum.BOOLEAN.getType();
        }
        if (isTinyint(columnType, columnDefinition)) {
            return TypeEnum.TINYINT.getType();
        }
        if (isSmallint(columnType, columnDefinition)) {
            return TypeEnum.SMALLINT.getType();
        }
        if (isInt(columnType, columnDefinition)) {
            return TypeEnum.INT.getType();
        }
        if (isLong(columnType, columnDefinition)) {
            return TypeEnum.BIGINT.getType();
        }
        if (isFloat(columnType, columnDefinition)) {
            return TypeEnum.FLOAT.getType();
        }
        if (isDouble(columnType, columnDefinition)) {
            return TypeEnum.DOUBLE.getType();
        }
        if (isDecimal(columnType, columnDefinition)) {
            return TypeEnum.DECIMAL.getType();
        }
        if (isVarchar(columnType, columnDefinition)) {
            return TypeEnum.VARCHAR.getType();
        }
        if (isDatetime(columnType, columnDefinition)) {
            return TypeEnum.DATETIME.getType();
        }
        if (isBlob(columnType, columnDefinition)) {
            return TypeEnum.BLOB.getType();
        }
        return TypeEnum.VARCHAR.getType();
    }

    default boolean contains(List<String> columnTypes, String type) {
        for (String columnType : columnTypes) {
            if (StringUtils.containsIgnoreCase(type, columnType)) {
                return true;
            }
        }
        return false;
    }

    boolean isBit(String columnType, ColumnDefinition columnDefinition);
    boolean isBoolean(String columnType, ColumnDefinition columnDefinition);
    boolean isTinyint(String columnType, ColumnDefinition columnDefinition);
    boolean isSmallint(String columnType, ColumnDefinition columnDefinition);
    boolean isInt(String columnType, ColumnDefinition columnDefinition);
    boolean isLong(String columnType, ColumnDefinition columnDefinition);
    boolean isFloat(String columnType, ColumnDefinition columnDefinition);
    boolean isDouble(String columnType, ColumnDefinition columnDefinition);
    boolean isDecimal(String columnType, ColumnDefinition columnDefinition);
    boolean isVarchar(String columnType, ColumnDefinition columnDefinition);
    boolean isDatetime(String columnType, ColumnDefinition columnDefinition);
    boolean isBlob(String columnType, ColumnDefinition columnDefinition);
}
