package com.github.alekseypetkun.TransportTicketApp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Сущность перевозчик
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Carrier implements Serializable {

    /**
     * Идентификатор перевозчика
     */
    private Long id;

    /**
     * Название компании
     */
    private String name;

    /**
     * Номер телефона компании
     */
    private String phone;
}
