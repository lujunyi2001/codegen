package org.sonic.codegen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@MapperScan("com.**.mapper")
@SpringBootApplication
public class GenApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(GenApplication.class, args);
	}

}
