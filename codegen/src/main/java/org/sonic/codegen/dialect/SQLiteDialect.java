package org.sonic.codegen.dialect;

import java.sql.Types;

import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.descriptor.sql.ClobTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

/**
* @author lujunyi
*/
public class SQLiteDialect extends org.sqlite.hibernate.dialect.SQLiteDialect {

	public SQLiteDialect() {
		super();
		registerColumnType(Types.CLOB, "text");
		registerHibernateType( Types.CLOB, StandardBasicTypes.TEXT.getName() );
		registerHibernateType( Types.NCLOB, StandardBasicTypes.TEXT.getName() );
	}
	
	@Override
	public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
		if(sqlTypeDescriptor instanceof ClobTypeDescriptor) {
			return new VarcharTypeDescriptor();
		}
		return super.remapSqlTypeDescriptor(sqlTypeDescriptor);
	} 
  
}
