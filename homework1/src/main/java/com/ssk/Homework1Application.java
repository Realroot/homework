package com.ssk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Homework1Application {

	@RequestMapping("")
	public String home() {
		return "Hello, Spring boot!";
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Homework1Application.class, args);
	}
}
