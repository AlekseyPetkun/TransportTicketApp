package com.example.ConsumerForTicketService.service;


import com.example.ConsumerForTicketService.dto.BuyerTicketDto;

public interface TicketService {

    void persistTicket(BuyerTicketDto dto);
    void persistUser(BuyerTicketDto dto);
    void persistRoute(BuyerTicketDto dto);
    void persistCarrier(BuyerTicketDto dto);

}
