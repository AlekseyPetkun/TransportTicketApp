package com.github.alekseypetkun.TransportTicketApp.repository;


import com.github.alekseypetkun.TransportTicketApp.model.Ticket;

import java.util.Map;

/**
 * Сервис по работе с Redis
 */
public interface RedisRepository {

    /**
     * Поиск всех билетов в Redis
     *
     * @return билеты
     */
    Map<String, Ticket> findAllTickets();

    /**
     * Сохранение билета в Redis
     *
     * @param ticket билет
     */
    void add(Ticket ticket);

    /**
     * Удаление билета из Redis по идентификатору
     *
     * @param id идентификатор
     */
    void delete(String id);

    /**
     * Поиск билета в Redis по идентификатору
     *
     * @param id идентификатор
     * @return найденный билет
     */
    Ticket findTicket(String id);
}
