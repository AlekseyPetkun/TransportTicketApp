package com.github.alekseypetkun.TransportTicketApp.service;


import com.github.alekseypetkun.TransportTicketApp.model.Ticket;

import java.util.List;

/**
 * Сервис фильтрации набора отправлений согласно различным правилам
 */
public interface FilterService {

    /**
     * Исключение отправлений до текущего момента времени из общего списка
     *
     * @param tickets общий список отправлений
     * @return отфильтрованный список без отправлений до текущего момента времени
     */
    List<Ticket> removeDepartureUpToTheCurrentPointInTime(List<Ticket> tickets);
}
