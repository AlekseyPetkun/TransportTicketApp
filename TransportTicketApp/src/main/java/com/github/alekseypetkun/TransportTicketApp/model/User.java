package com.github.alekseypetkun.TransportTicketApp.model;

import com.github.alekseypetkun.TransportTicketApp.constant.Role;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Сущность пользователь
 */
@Data
@Builder(toBuilder = true)
// Генерирует метод toBuilder(), который создает копию объекта класса и позволяет изменять значения полей копии объекта без изменения исходного объекта.
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    public User(Long id, String username, String password, String email, String firstName, String lastName, boolean enabled, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Идентификатор пользователя
     */
    private Long id;

    /**
     * Логин пользователя
     */
    private String username;

    /**
     * Пароль пользователя
     */
    private String password;

    /**
     * Почта пользователя
     */
    private String email;

    /**
     * Роль пользователя для авторизации
     */
    private Role role;

    /**
     * Имя пользователя
     */
    private String firstName;

    /**
     * Фамилия пользователя
     */
    private String lastName;

    /**
     * Активация/деактивация пользователя
     */
    private boolean enabled;

    /**
     * Дата и время создания пользователя
     */
    private LocalDateTime createdAt;

    /**
     * Дата и время изменения пользователя
     */
    private LocalDateTime updatedAt;

    @ToString.Include(name = "password") // Строковое представление этого поля будет засекречено
    private String maskPassword() {
        return "*****";
    }


}
