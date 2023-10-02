package com.example.ConsumerForTicketService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность перевозчик
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carriers")
public class Carrier {

    /**
     * Идентификатор перевозчика
     */
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
