package com.github.alekseypetkun.TransportTicketApp.exception;

/**
 * Ошибка проверки наличия сущности в БД
 */
public class EntityAlreadyExistsException extends RuntimeException{

    public EntityAlreadyExistsException(String text) {
        super("Сущность с параметром: " + text + " уже существует!");
    }
}

