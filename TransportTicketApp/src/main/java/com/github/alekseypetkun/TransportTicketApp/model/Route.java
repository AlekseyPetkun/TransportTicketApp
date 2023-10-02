package com.github.alekseypetkun.TransportTicketApp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Сущность маршрут
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Route implements Serializable {

    /**
     * Идентификатор маршрута
     */
    private Long id;

    /**
     * Пункт отправления
     */
    private String departure;

    /**
     * Пункт назначения
     */
    private String destination;

    /**
     * Перевозчик
     */
    private Carrier carrier;

    /**
     * Время в пути (мин)
     */
    private Integer durations_min;
}
