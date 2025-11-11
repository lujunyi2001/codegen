package org.sonic.codegen.gen;

import java.util.HashMap;
import java.util.Map;

import org.sonic.codegen.gen.mysql.MySqlService;
import org.sonic.codegen.gen.oracle.OracleService;
import org.sonic.codegen.gen.postgresql.PostgreSqlService;
import org.sonic.codegen.gen.sqlserver.SqlServerService;

public class SQLServiceFactory {


    private static final Map<Integer, SQLService> SERVICE_CONFIG = new HashMap<>(16);

    static {
        SERVICE_CONFIG.put(DbType.MYSQL.getType(), new MySqlService());
        SERVICE_CONFIG.put(DbType.ORACLE.getType(), new OracleService());
        SERVICE_CONFIG.put(DbType.SQL_SERVER.getType(), new SqlServerService());
        SERVICE_CONFIG.put(DbType.POSTGRE_SQL.getType(), new PostgreSqlService());
    }

    public static SQLService build(GeneratorConfig generatorConfig) {
        SQLService service = SERVICE_CONFIG.get(generatorConfig.getDbType());
        if (service == null) {
            throw new RuntimeException("本系统暂不支持该数据源(" + generatorConfig.getDriverClass() + ")");
        }
        return service;
    }

}
