package com.github.alekseypetkun.TransportTicketApp.service;


import com.github.alekseypetkun.TransportTicketApp.dto.*;
import com.github.alekseypetkun.TransportTicketApp.model.User;

/**
 * Сервис по работе с пользователями
 */
public interface UserService {

    /**
     * Получить пользователя по id
     *
     * @param id идентификатор пользователя
     * @return найденный пользователь
     */
    UserDto getUserById(Long id);

    /**
     * Получить id аутентифицированного пользователя
     *
     * @return идентификатор аутентифицированного пользователя
     */
    Long getAuthenticatedUserId();

    /**
     * Поиск пользователя по его идентификатору
     *
     * @param userId идентификатор пользователя
     * @return найденный пользователь
     */
    User findUserById(Long userId);

    UserDto updateUser(UpdateUserDto dto, Long userId);

    ResponseWrapperUsers getAllUsers(Integer pageNumber, Integer pageSize);

    void deleteUser(Long userId);
}
