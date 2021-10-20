package com.abc.TMDT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import com.abc.repository.TaikhoanRepository;

import jdk.javadoc.doclet.Reporter;

@SpringBootApplication
//@EnableJdbcHttpSession
@EntityScan("com.abc.entity")
@EnableJpaRepositories("com.abc.repository")
@ComponentScan({"com.abc.controller","com.abc.jwt.configs","com.abc.service"})
public class TmdtApplication {
	
	
	public static void main(String[] args) {
		SpringApplication.run(TmdtApplication.class, args);
		
	}

}
