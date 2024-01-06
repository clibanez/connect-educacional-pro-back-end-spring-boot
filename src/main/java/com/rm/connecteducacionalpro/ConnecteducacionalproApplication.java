package com.rm.connecteducacionalpro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ConnecteducacionalproApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ConnecteducacionalproApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
		return application.sources(ConnecteducacionalproApplication.class);
	}

}
