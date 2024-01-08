package com.telusko.quizapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class QuizappApplication {

	public static void main(String[] args) {
	    SpringApplicationBuilder builder = new SpringApplicationBuilder(QuizappApplication.class);
	    builder.headless(false);
	    ConfigurableApplicationContext context = builder.run(args);
	}

}
