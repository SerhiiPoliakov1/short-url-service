package com.shorturlservice.shorturl;

import com.shorturlservice.shorturl.controller.ShorterController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages={
		"src.main.java.com.shorturlservice.shorturl.model"})
@ComponentScan(basePackageClasses = ShorterController.class)
public class ShortUrlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortUrlApplication.class, args);
	}

}
