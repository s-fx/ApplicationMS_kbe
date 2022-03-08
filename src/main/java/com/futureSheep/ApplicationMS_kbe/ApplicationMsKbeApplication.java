package com.futureSheep.ApplicationMS_kbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@EnableCaching
@SpringBootApplication
public class ApplicationMsKbeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationMsKbeApplication.class, args);
	}

}
