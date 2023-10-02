package com.github.alekseypetkun.TransportTicketApp.exception;


public class NotFoundUserException extends NotFoundException{

    public NotFoundUserException(String username) {
        super("Пользователь с логином: " + username + " не найден!");
    }

    public NotFoundUserException(long userId) {
        super("Пользователь с id: " + userId + " не найден!");
    }
}
