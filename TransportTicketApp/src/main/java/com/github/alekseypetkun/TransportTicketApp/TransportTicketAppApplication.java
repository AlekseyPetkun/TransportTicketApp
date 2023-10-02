package com.github.alekseypetkun.TransportTicketApp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "TransportTicketsMicroservicesApplication",
		description = "Веб-приложение для предоставления возможностей пользователям покупать транспортные билеты"
))
public class TransportTicketAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransportTicketAppApplication.class, args);
	}

}
