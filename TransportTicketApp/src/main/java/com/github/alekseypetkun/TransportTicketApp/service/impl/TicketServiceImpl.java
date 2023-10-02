package com.github.alekseypetkun.TransportTicketApp.service.impl;

import com.github.alekseypetkun.TransportTicketApp.constant.Reserved;
import com.github.alekseypetkun.TransportTicketApp.dao.impl.TicketDaoImpl;
import com.github.alekseypetkun.TransportTicketApp.dto.*;
import com.github.alekseypetkun.TransportTicketApp.exception.*;
import com.github.alekseypetkun.TransportTicketApp.mapper.TicketMapper;
import com.github.alekseypetkun.TransportTicketApp.model.*;
import com.github.alekseypetkun.TransportTicketApp.producer.ProducerService;
import com.github.alekseypetkun.TransportTicketApp.repository.RedisRepository;
import com.github.alekseypetkun.TransportTicketApp.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Бизнес-логика по работе с билетами
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketDaoImpl ticketDao;
    private final UserService userService;
    private final AuthService authService;
    private final TicketMapper ticketMapper;
    private final RedisRepository redisRepository;
    private final ProducerService producerService;

    @Override
    public TicketDto createTicket(CreateTicket dto, Long userId) {

        User admin = userService.findUserById(userId);

        if (authService.checkAccess(admin)) {

            Ticket ticket = ticketMapper.map(dto);

            ticket.setReserved(Reserved.FREE);

            TicketDto newTicket = ticketMapper.map(ticketDao.addEntity(ticket));
            log.info("IN createTicket - ticket: {} created", ticket);
            return newTicket;
        } else {
            throw new AuthorisationException("user is forbidden", "8070_USER_IS_FORBIDDEN");
        }
    }

    @Override
    public BuyerTicketDto buyTicket(Long ticketId, Long userId) {

        User buyer = userService.findUserById(userId);
        Ticket ticket = findTicketById(ticketId);

        if (buyer.isEnabled() && ticket.getReserved().equals(Reserved.FREE)) {

            ticket.setUser(buyer);
            ticket.setReserved(Reserved.BUSY);

            redisRepository.add(ticket);
            ticketDao.buyTicket(ticket);
            BuyerTicketDto buyTicket = ticketMapper.mapToBuyer(ticket);

            producerService.sendMessage(buyTicket);
            log.info("IN buyTicket - ticket: {} buyed", ticket);
            return buyTicket;
        } else {
            throw new AuthorisationException("user is forbidden", "8070_USER_IS_FORBIDDEN");
        }
    }

    @Override
    public void canselBuyTicket(Long ticketId, Long userId) {

        User buyer = userService.findUserById(userId);
        Ticket ticket = findTicketById(ticketId);

        if (buyer.isEnabled() && Objects.equals(ticket.getUser().getId(), buyer.getId())) {

            ticket.setUser(null);
            ticket.setReserved(Reserved.FREE);

            redisRepository.delete(String.valueOf(ticket.getId()));
            ticketDao.canselTicket(ticket);

            log.info("IN updateTicket - ticket: {} canseled", ticket);

        } else {
            throw new AuthorisationException("user is forbidden", "8070_USER_IS_FORBIDDEN");
        }
    }

    @Override
    public TicketDto updateTicket(UpdateTicket dto, Long ticketId, Long userId) {

        User admin = userService.findUserById(userId);

        if (authService.checkAccess(admin)) {

            Ticket ticket = findTicketById(ticketId);

            ticketMapper.patch(dto, ticket);

            ticketDao.updateEntity(ticket);
            TicketDto updatedTicket = ticketMapper.map(ticket);

            log.info("IN updateTicket - ticket: {} updated", ticket);
            return updatedTicket;
        } else {
            throw new AuthorisationException("user is forbidden", "8070_USER_IS_FORBIDDEN");
        }
    }

    @Override
    public ResponseWrapperBuyerTickets getAllMeTickets(Long userId, Integer pageNumber, Integer pageSize) {

//        List<BuyerTicketDto> dtoList = ticketDao
//                .findAllByBuyer(userId, pageNumber, pageSize).stream()
//                .map(ticketMapper::mapToBuyer)
//                .toList();
        Long totalAmount = ticketDao.count(userId);

        List<Ticket> redisTickets = redisRepository.findAllTickets().values()
                .stream()
                .toList();

        List<Ticket> resultTickets = null;
        if (redisTickets.size() != totalAmount) {
            List<Ticket> tickets = ticketDao.findAllByBuyer(userId, pageNumber, pageSize);
            tickets.forEach(value -> {
                if (!redisTickets.contains(value)) {
                    redisRepository.add(value);
                }
            });
            redisTickets.forEach(
                    value -> {
                        if (value.getUser() == null) {
                            redisRepository.delete(String.valueOf(value.getId()));
                        }
                    }
            );
            resultTickets = ticketDao.findAllByBuyer(userId, pageNumber, pageSize);
        } else {
            resultTickets = redisRepository.findAllTickets().values().stream().toList();
        }

        List<BuyerTicketDto> dtoList = resultTickets.stream()
                .map(ticketMapper::mapToBuyer)
                .toList();

        return new ResponseWrapperBuyerTickets(totalAmount, dtoList);
    }

    @Override
    public ResponseWrapperTickets getAllTickets(Integer pageNumber, Integer pageSize) {

        Long totalAmount = ticketDao.count();

        List<TicketDto> dtoList = ticketDao
                .returnAll(pageNumber, pageSize).stream()
                .map(ticketMapper::map)
                .toList();

        return new ResponseWrapperTickets(totalAmount, dtoList);
    }

    @Override
    public Ticket findTicketById(Long ticketId) {

        Ticket ticket = ticketDao.searchById(ticketId);
        if (ticket == null) {
            throw new NotFoundTicketException(ticketId);
        }
        return ticket;
    }

    @Override
    public void deleteTicket(Long ticketId, Long userId) {

        User admin = userService.findUserById(userId);

        if (authService.checkAccess(admin)) {

            Ticket ticket = findTicketById(ticketId);

            ticketDao.deleteEntityById(ticketId);
            log.info("IN deleteTicket - ticket: {} deleted", ticket);
        } else {
            throw new AuthorisationException("user is forbidden", "8070_USER_IS_FORBIDDEN");
        }
    }
}
