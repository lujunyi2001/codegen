package org.sonic.codegen.gen.postgresql;

import org.sonic.codegen.gen.GeneratorConfig;
import org.sonic.codegen.gen.SQLService;
import org.sonic.codegen.gen.TableSelector;

/**
 * @author tanghc
 */
public class PostgreSqlService implements SQLService {
    @Override
    public TableSelector getTableSelector(GeneratorConfig generatorConfig) {
        return new PostgreSqlTableSelector(new PostgreSqlColumnSelector(generatorConfig), generatorConfig);
    }

}
