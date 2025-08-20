package com.example.registrationProject;

import io.github.cdimascio.dotenv.Dotenv;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "Streaming & Manage Music Content API",
				version = "1.0",
				description = "API documentation for the  Streaming & Manage Music Content project",
				contact = @Contact(
						name = "Ujjwal Kumar",
						url = "https://github.com/ujjwalkumar-64"
		)
))

@SpringBootApplication
public class RegistrationProjectApplication {


	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
		);

		SpringApplication.run(RegistrationProjectApplication.class, args);


	}


}
