package com.github.alekseypetkun.TransportTicketApp.service.impl;

import com.github.alekseypetkun.TransportTicketApp.model.Ticket;
import com.github.alekseypetkun.TransportTicketApp.service.FilterService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * Бизнес-логика по работе с фильтрацией набора отправлений согласно различным правилам
 */
@Service
public class FilterServiceImpl implements FilterService {

    TimeZone timeZone = TimeZone.getDefault();
    LocalDateTime localDateTime = LocalDateTime.now(timeZone.toZoneId());

    @Override
    public List<Ticket> removeDepartureUpToTheCurrentPointInTime(List<Ticket> tickets) {

        return tickets.stream()
                .filter(ticket -> ticket.getDepartureDateTime().isAfter(localDateTime))
                .collect(Collectors.toList());
    }
}
