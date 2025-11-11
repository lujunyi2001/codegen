package org.sonic.codegen.config;

import java.util.Collections;
import java.util.Map;

import org.hibernate.jpa.boot.spi.IntegratorProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;
import org.sqlite.hibernate.dialect.SQLiteDialect;

/**
* @author lujunyi
*/
@Component
public class HibernateConfig implements HibernatePropertiesCustomizer {
    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        if(hibernateProperties.get("hibernate.dialect") != null){
            try {
                if(SQLiteDialect.class.isAssignableFrom(Class.forName((String) hibernateProperties.get("hibernate.dialect")))){
                    return ;
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        hibernateProperties.put("hibernate.use_sql_comments", true);
        hibernateProperties.put("hibernate.integrator_provider",
                (IntegratorProvider) () -> Collections.singletonList(CommentIntegrator.INSTANCE));
    }
}