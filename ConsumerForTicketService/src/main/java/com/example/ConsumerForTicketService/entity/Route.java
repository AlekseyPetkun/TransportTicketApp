package com.example.ConsumerForTicketService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность маршрут
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "routs")
public class Route {

    /**
     * Идентификатор маршрута
     */
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "carrier")
    private Carrier carrier;

    /**
     * Время в пути (мин)
     */
    private Integer durations_min;
}
