package org.sonic.codegen.gen.mysql;

import org.sonic.codegen.gen.GeneratorConfig;
import org.sonic.codegen.gen.SQLService;
import org.sonic.codegen.gen.TableSelector;

public class MySqlService implements SQLService {

	@Override
	public TableSelector getTableSelector(GeneratorConfig generatorConfig) {
		return new MySqlTableSelector(new MySqlColumnSelector(generatorConfig), generatorConfig);
	}

}
