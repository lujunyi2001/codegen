package org.sonic.codegen.gen.oracle;

import org.sonic.codegen.gen.GeneratorConfig;
import org.sonic.codegen.gen.SQLService;
import org.sonic.codegen.gen.TableSelector;

public class OracleService implements SQLService {

	@Override
	public TableSelector getTableSelector(GeneratorConfig generatorConfig) {
		return new OracleTableSelector(new OracleColumnSelector(generatorConfig), generatorConfig);
	}

}
