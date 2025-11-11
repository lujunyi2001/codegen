package org.sonic.codegen.gen.sqlserver;

import org.sonic.codegen.gen.GeneratorConfig;
import org.sonic.codegen.gen.SQLService;
import org.sonic.codegen.gen.TableSelector;

public class SqlServerService implements SQLService {

	@Override
	public TableSelector getTableSelector(GeneratorConfig generatorConfig) {
		return new SqlServerTableSelector(new SqlServerColumnSelector(generatorConfig), generatorConfig);
	}

}
