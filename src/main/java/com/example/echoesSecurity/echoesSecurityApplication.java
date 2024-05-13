package com.example.echoesSecurity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.example")
@MapperScan("com.example.echoesSecurity.dao")
@SpringBootApplication
public class echoesSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(echoesSecurityApplication.class, args);
	}

}
