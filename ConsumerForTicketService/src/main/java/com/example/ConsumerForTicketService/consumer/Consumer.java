package com.example.ConsumerForTicketService.consumer;

import com.example.ConsumerForTicketService.dto.BuyerTicketDto;
import com.example.ConsumerForTicketService.service.TicketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Consumer {

    private static final String orderTopic = "t.ticket.order";

    private final ObjectMapper objectMapper;
    private final TicketService ticketService;

    @KafkaListener(topics = orderTopic)
    public void consumeMessage(String message) {
        log.info("message consumed {}", message);

        BuyerTicketDto buyerTicketDto;

        try {
            buyerTicketDto = objectMapper.readValue(message, BuyerTicketDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ticketService.persistTicket(buyerTicketDto);
        ticketService.persistUser(buyerTicketDto);
        ticketService.persistRoute(buyerTicketDto);
        ticketService.persistCarrier(buyerTicketDto);
    }

}
