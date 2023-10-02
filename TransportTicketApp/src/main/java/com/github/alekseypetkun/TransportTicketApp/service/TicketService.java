package com.github.alekseypetkun.TransportTicketApp.service;


import com.github.alekseypetkun.TransportTicketApp.dto.*;
import com.github.alekseypetkun.TransportTicketApp.model.Ticket;

/**
 * Сервис по работе с билетами
 */
public interface TicketService {

    TicketDto createTicket(CreateTicket dto, Long userId);

    BuyerTicketDto buyTicket(Long ticketId, Long userId);

    void canselBuyTicket(Long ticketId, Long userId);

    TicketDto updateTicket(UpdateTicket dto, Long ticketId, Long userId);

    ResponseWrapperBuyerTickets getAllMeTickets(Long userId, Integer pageNumber, Integer pageSize);

    ResponseWrapperTickets getAllTickets(Integer pageNumber, Integer pageSize);

    Ticket findTicketById(Long ticketId);

    void deleteTicket(Long ticketId, Long userId);
}
