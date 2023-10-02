package com.github.alekseypetkun.TransportTicketApp.service;


import com.github.alekseypetkun.TransportTicketApp.dto.*;
import com.github.alekseypetkun.TransportTicketApp.model.Carrier;

/**
 * Сервис по работе с перевозчиками
 */
public interface CarrierService {

    CarrierDto createCarrier(CreateCarrier dto, Long userId);

    /**
     * Получить перевозчика по id
     *
     * @param carrierId идентификатор перевозчика
     * @return найденный перевозчик
     */
    Carrier findCarrierById(Long carrierId);

    CarrierDto updateCarrier(UpdateCarrier dto, Long carrierId, Long userId);

    ResponseWrapperCarriers getAllCarriers(Integer pageNumber, Integer pageSize, Long userId);

    void deleteCarrier(Long carrierId, Long userId);
}
