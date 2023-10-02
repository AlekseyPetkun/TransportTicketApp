package com.github.alekseypetkun.TransportTicketApp.model;

import com.github.alekseypetkun.TransportTicketApp.constant.Reserved;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Сущность билет
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Component
//@RedisHash("Ticket")
public class Ticket implements Serializable {

    public Ticket(Long id,
                  Route route,
                  LocalDateTime departureDateTime,
                  Integer placeNumber,
                  Integer price,
                  Reserved reserved) {

        this.id = id;
        this.route = route;
        this.departureDateTime = departureDateTime;
        this.placeNumber = placeNumber;
        this.price = price;
        this.reserved = reserved;
    }

    /**
     * Идентификатор билета
     */
    private Long id;

    /**
     * Маршрут
     */
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
     * Резерв
     */
    private Reserved reserved;

    /**
     * Маршрут
     */
    private User user;
}
