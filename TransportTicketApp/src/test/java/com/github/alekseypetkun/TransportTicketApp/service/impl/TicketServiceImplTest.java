package com.github.alekseypetkun.TransportTicketApp.service.impl;

import com.github.alekseypetkun.TransportTicketApp.constant.Reserved;
import com.github.alekseypetkun.TransportTicketApp.constant.Role;
import com.github.alekseypetkun.TransportTicketApp.dao.impl.TicketDaoImpl;
import com.github.alekseypetkun.TransportTicketApp.dto.BuyerTicketDto;
import com.github.alekseypetkun.TransportTicketApp.dto.CreateTicket;
import com.github.alekseypetkun.TransportTicketApp.dto.RouteDto;
import com.github.alekseypetkun.TransportTicketApp.dto.TicketDto;
import com.github.alekseypetkun.TransportTicketApp.mapper.TicketMapper;
import com.github.alekseypetkun.TransportTicketApp.model.Ticket;
import com.github.alekseypetkun.TransportTicketApp.model.User;
import com.github.alekseypetkun.TransportTicketApp.producer.ProducerService;
import com.github.alekseypetkun.TransportTicketApp.repository.RedisRepository;
import com.github.alekseypetkun.TransportTicketApp.service.AuthService;
import com.github.alekseypetkun.TransportTicketApp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketDaoImpl ticketDao;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private RedisRepository redisRepository;

    @Mock
    private ProducerService producerService;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    void createTicket() {

        // Arrange
        CreateTicket createTicket = new CreateTicket();
        createTicket.setPlaceNumber(1);
        createTicket.setDepartureDateTime(LocalDateTime.parse("2022-01-01T10:00:00"));
        createTicket.setPrice(100);
        createTicket.setRouteId(1L);

        User admin = new User();
        admin.setId(1L);
        admin.setRole(Role.ADMIN);

        when(userService.findUserById(1L)).thenReturn(admin);
        when(authService.checkAccess(admin)).thenReturn(true);

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setDepartureDateTime(LocalDateTime.parse("2022-01-01T10:00:00"));
        ticket.setPrice(100);
        ticket.setPlaceNumber(1);
        ticket.setReserved(Reserved.FREE);

        TicketDto expectedDto = new TicketDto(1L, new RouteDto(), LocalDateTime.parse("2022-01-01T10:00:00"), 1, 100, Reserved.FREE);

        when(ticketMapper.map(createTicket)).thenReturn(ticket);
        when(ticketMapper.map(ticket)).thenReturn(expectedDto);
        when(ticketDao.addEntity(ticket)).thenReturn(ticket);

        // Act
        TicketDto result = ticketService.createTicket(createTicket, 1L);

        // Assert
        assertEquals(expectedDto, result);
    }

    @Test
    void buyTicket() {

//        // Arrange
//        Long ticketId = 1L;
//        Long userId = 1L;
//        User buyer = new User();
//        buyer.setId(userId);
//        buyer.setEnabled(true);
//
//        Ticket ticket = new Ticket();
//        ticket.setId(ticketId);
//        ticket.setReserved(Reserved.FREE);
//
//        when(userService.findUserById(userId)).thenReturn(buyer);
//        when(findTicketById(ticketId)).thenReturn(ticket);
//        when(ticketMapper.mapToBuyer(ticket)).thenReturn(new BuyerTicketDto());
//        when(redisRepository.add(ticket)).thenReturn(true);
//        when(ticketDao.buyTicket(ticket)).thenReturn(true);
//        when(producerService.sendMessage(new BuyerTicketDto())).thenReturn(true);
//
//        // Act
//        BuyerTicketDto result = ticketService.buyTicket(ticketId, userId);
//
//        // Assert
//        assertEquals(new BuyerTicketDto(), result);
    }

    @Test
    void canselBuyTicket() {
    }

    @Test
    void updateTicket() {
    }

    @Test
    void getAllMeTickets() {
    }

    @Test
    void getAllTickets() {
    }

    @Test
    void findTicketById() {
    }

    @Test
    void deleteTicket() {
    }
}