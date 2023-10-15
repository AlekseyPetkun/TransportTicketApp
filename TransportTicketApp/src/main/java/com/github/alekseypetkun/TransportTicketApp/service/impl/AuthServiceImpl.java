package com.github.alekseypetkun.TransportTicketApp.service.impl;

import com.github.alekseypetkun.TransportTicketApp.constant.Role;
import com.github.alekseypetkun.TransportTicketApp.dao.impl.UserDaoImpl;
import com.github.alekseypetkun.TransportTicketApp.dto.*;
import com.github.alekseypetkun.TransportTicketApp.exception.AuthenticationException;
import com.github.alekseypetkun.TransportTicketApp.exception.EntityAlreadyExistsException;
import com.github.alekseypetkun.TransportTicketApp.mapper.UserMapper;
import com.github.alekseypetkun.TransportTicketApp.model.*;
import com.github.alekseypetkun.TransportTicketApp.security.PBFDK2PasswordEncoder;
import com.github.alekseypetkun.TransportTicketApp.security.custom.CustomUserDetails;
import com.github.alekseypetkun.TransportTicketApp.security.custom.CustomUserDetailsService;
import com.github.alekseypetkun.TransportTicketApp.security.jwt.*;
import com.github.alekseypetkun.TransportTicketApp.service.AuthService;
import com.github.alekseypetkun.TransportTicketApp.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Бизнес-логика по регистрации и аутентификации
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomUserDetailsService customUserDetailsService;
    private final PBFDK2PasswordEncoder passwordEncoder;

    private final Map<String, String> refreshTokenStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final UserDaoImpl userDao;
    private final UserMapper userMapper;

    TimeZone timeZone = TimeZone.getDefault();
    LocalDateTime localDateTime = LocalDateTime.now(timeZone.toZoneId());

    @Override
    public UserDto registerUser(RegisterRequest dto) {

        User check = userDao.findByUsername(dto.getEmail());
        if (check != null) {
            throw new EntityAlreadyExistsException(dto.getEmail());
        }

        User user = userMapper.map(dto);
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUsername(dto.getEmail());
        user.setRole(Role.USER);
        user.setCreatedAt(localDateTime);
        user.setUpdatedAt(localDateTime);

        UserDto newUser = userMapper.map(userDao.addEntity(user));
        log.info("IN registerUser - user: {} created", user);
        return newUser;
    }

    @Override
    public LoginResponse login(LoginRequest authRequest) {

        return checkToken(authRequest.getUsername(),
                authRequest.getPassword());
    }

    /**
     * Выдача валидного токена аутентифицированного пользователя
     *
     * @param username логин пользователя
     * @param password пароль пользователя
     * @return валидный токен пользователя
     */
    private LoginResponse checkToken(String username, String password) {

        final CustomUserDetails user = (CustomUserDetails) customUserDetailsService
                .loadUserByUsername(username);

        if (user == null) {
            throw new AuthenticationException("Invalid username", "8070_INVALID_USERNAME");
        }

        if (!user.isEnabled()) {
            throw new AuthenticationException("Account disabled", "8070_USER_ACCOUNT_DISABLED");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Invalid password", "8070_INVALID_PASSWORD");
        }

        final String accessToken = jwtProvider.generateAccessToken(user);
        final String refreshToken = jwtProvider.generateRefreshToken(user);

        refreshTokenStorage.put(String.valueOf(user.getUserById()), refreshToken);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserById())
                .build();
    }

    @Override
    public LoginResponse getAccessToken(String refreshToken) {

        final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        final String userId = claims.getSubject();
        final String saveRefreshToken = refreshTokenStorage.get(userId);
        final CustomUserDetails user = (CustomUserDetails) customUserDetailsService
                .loadUserById(Long.valueOf(userId));

        if (jwtProvider.validateRefreshToken(refreshToken)) {

            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {

                final String accessToken = jwtProvider.generateAccessToken(user);

                return new LoginResponse(user.getUserById(), accessToken, null);
            }
        }
        return new LoginResponse(user.getUserById(), null, null);
    }

    @Override
    public LoginResponse getNewRefreshToken(String refreshToken) throws AuthenticationException {

        if (jwtProvider.validateRefreshToken(refreshToken)) {

            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String userId = claims.getSubject();
            final String saveRefreshToken = refreshTokenStorage.get(userId);

            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {

                final CustomUserDetails user = (CustomUserDetails) customUserDetailsService.loadUserById(Long.valueOf(userId));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);

                refreshTokenStorage.put(String.valueOf(user.getUserById()), newRefreshToken);

                return new LoginResponse(user.getUserById(), accessToken, newRefreshToken);
            }
        }
        throw new AuthenticationException("Invalid jwt token", "8070_INVALID_JWT_TOKEN");
    }

    @Override
    public JwtAuthentication getAuthInfo() {

        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public boolean checkAccess(User user) {

        return user.isEnabled()
                && user.getRole() == Role.ADMIN;
    }
}
