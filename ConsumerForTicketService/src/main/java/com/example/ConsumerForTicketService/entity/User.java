package com.example.ConsumerForTicketService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;

/**
 * Сущность пользователь
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    /**
     * Идентификатор пользователя
     */
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя
     */
    private String firstName;

    /**
     * Фамилия пользователя
     */
    private String lastName;


    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Ticket> tickets;

    @ToString.Include(name = "password") // Строковое представление этого поля будет засекречено
    private String maskPassword() {
        return "*****";
    }


}
