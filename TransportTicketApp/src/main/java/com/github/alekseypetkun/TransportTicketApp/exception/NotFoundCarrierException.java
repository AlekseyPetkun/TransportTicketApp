package com.github.alekseypetkun.TransportTicketApp.exception;

public class NotFoundCarrierException extends NotFoundException{

    public NotFoundCarrierException(Long id) {
        super("Перевозчик с id: " + id + " не найден!");
    }
}
