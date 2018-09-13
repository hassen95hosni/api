package com.vistaprint.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ApiRethinkdbApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiRethinkdbApplication.class, args);
		System.setProperty("server.port","0");
       // System.setProperty("server.tomcat.max-threads","200"));
        //System.setProperty("server.connection-timeout","60000"));
		ApplicationContext  context =SpringApplication.run(ApiRethinkdbApplication.class, args);
		System.out.println("Contains A  "+context.
                containsBeanDefinition("demoBeanA")+context.
                containsBeanDefinition("Bean B"));
		for (String name: context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
	
	}
}
