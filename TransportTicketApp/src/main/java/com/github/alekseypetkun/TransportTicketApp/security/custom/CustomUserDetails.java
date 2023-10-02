package com.github.alekseypetkun.TransportTicketApp.security.custom;

import com.github.alekseypetkun.TransportTicketApp.constant.Role;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Сущность Spring Security с параметрами пользователя
 */

public interface CustomUserDetails extends UserDetails {

    Long getUserById(); // Идентификатор пользователя
    Role getUserByRole(); // Роль пользователя
}
