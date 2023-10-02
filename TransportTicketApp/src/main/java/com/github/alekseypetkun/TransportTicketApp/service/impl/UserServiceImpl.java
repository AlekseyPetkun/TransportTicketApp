package com.github.alekseypetkun.TransportTicketApp.service.impl;

import com.github.alekseypetkun.TransportTicketApp.dao.impl.UserDaoImpl;
import com.github.alekseypetkun.TransportTicketApp.dto.*;
import com.github.alekseypetkun.TransportTicketApp.exception.*;
import com.github.alekseypetkun.TransportTicketApp.mapper.UserMapper;
import com.github.alekseypetkun.TransportTicketApp.model.*;
import com.github.alekseypetkun.TransportTicketApp.security.jwt.JwtAuthentication;
import com.github.alekseypetkun.TransportTicketApp.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Бизнес-логика по работе с пользователями
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDaoImpl userDao;
    private final UserMapper userMapper;
    private final AuthService authService;

    TimeZone timeZone = TimeZone.getDefault();
    LocalDateTime localDateTime = LocalDateTime.now(timeZone.toZoneId());

    @Override
    public UserDto getUserById(Long id) {

        User user = userDao.searchById(id);
        if (user == null) {
            throw new NotFoundUserException(id);
        }
        return userMapper.map(user);
    }

    @Override
    public Long getAuthenticatedUserId() {

        JwtAuthentication authInfo = authService.getAuthInfo();
        return Long.valueOf(authInfo.getName());
    }

    @Override
    public User findUserById(Long userId) {

        User user = userDao.searchById(userId);
        if (user == null) {
            throw new NotFoundUserException(userId);
        }
        return user;
    }

    @Override
    public UserDto updateUser(UpdateUserDto dto, Long userId) {

        User user = findUserById(userId);

        userMapper.patch(dto, user);

        user.setUpdatedAt(localDateTime);

        userDao.updateEntity(user);
        UserDto updatedUser = userMapper.map(user);

        log.info("IN updateUser - user: {} updated", user);
        return updatedUser;
    }

    @Override
    public ResponseWrapperUsers getAllUsers(Integer pageNumber, Integer pageSize) {

        Long totalAmount = userDao.count();
        List<UserDto> dtoList = new ArrayList<>(userDao
                .returnAll(pageNumber, pageSize)).stream()
                .map(userMapper::map)
                .toList();

        return new ResponseWrapperUsers(totalAmount, dtoList);
    }

    @Override
    public void deleteUser(Long userId) {

        User authorOrAdmin = findUserById(userId);

        if (authService.checkAccess(authorOrAdmin)) {

            userDao.deleteEntityById(userId);
            log.info("IN deleteUser - user: {} deleted", authorOrAdmin);
        } else {
            throw new AuthorisationException("user is forbidden", "8070_USER_IS_FORBIDDEN");
        }
    }
}
