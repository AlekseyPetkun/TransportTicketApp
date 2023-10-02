package com.example.ConsumerForTicketService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Сущность билет
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tickets")
public class Ticket {

    /**
     * Идентификатор билета
     */
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Маршрут
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "route")
    private Route route;

    /**
     * Дата и время отправления
     */
    private LocalDateTime departureDateTime;

    /**
     * Номер места
     */
    private Integer placeNumber;

    /**
     * Стоимость билета
     */
    private Integer price;

    /**
     * Покупатель
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "buyer_id")
    private User user;
}
