package com.github.alekseypetkun.TransportTicketApp.exception;

public class NotFoundRouteException extends NotFoundException {

    public NotFoundRouteException(Long id) {
        super("Маршрут с id: " + id + " не найден!");
    }
}
