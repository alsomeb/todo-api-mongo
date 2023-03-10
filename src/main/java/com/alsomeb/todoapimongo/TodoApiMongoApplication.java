package com.alsomeb.todoapimongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2 // Swagger UI
public class TodoApiMongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoApiMongoApplication.class, args);
	}

}
