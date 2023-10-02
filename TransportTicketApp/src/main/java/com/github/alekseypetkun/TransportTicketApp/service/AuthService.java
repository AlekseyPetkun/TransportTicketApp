package com.github.alekseypetkun.TransportTicketApp.service;


import com.github.alekseypetkun.TransportTicketApp.dto.*;
import com.github.alekseypetkun.TransportTicketApp.model.User;
import com.github.alekseypetkun.TransportTicketApp.security.jwt.JwtAuthentication;

/**
 * Сервис по работе с аутентификацией и регистрацией.
 */
public interface AuthService {

    UserDto registerUser(RegisterRequest dto);

    LoginResponse login(LoginRequest authRequest);

    LoginResponse getAccessToken(String refreshToken);

    LoginResponse getNewRefreshToken(String refreshToken);

    JwtAuthentication getAuthInfo();

    boolean checkAccess(User user);
}
