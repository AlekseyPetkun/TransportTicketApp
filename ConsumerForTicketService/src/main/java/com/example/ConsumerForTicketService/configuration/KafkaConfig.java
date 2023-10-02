package com.example.ConsumerForTicketService.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
