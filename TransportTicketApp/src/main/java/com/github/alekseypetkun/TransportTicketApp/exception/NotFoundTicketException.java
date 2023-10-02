package com.github.alekseypetkun.TransportTicketApp.exception;

public class NotFoundTicketException extends NotFoundException {

    public NotFoundTicketException(Long id) {
        super("Билет с id: " + id + " не найден!");
    }
}
