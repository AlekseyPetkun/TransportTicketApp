package com.github.alekseypetkun.TransportTicketApp.service;


import com.github.alekseypetkun.TransportTicketApp.dto.*;
import com.github.alekseypetkun.TransportTicketApp.model.Route;

/**
 * Сервис по работе с маршрутами
 */
public interface RouteService {

    RouteDto createRoute(CreateRoute dto, Long userId);

    /**
     * Получить маршрут по id
     *
     * @param routeId идентификатор маршрута
     * @return найденный маршрут
     */
    Route findRouteById(Long routeId);

    RouteDto updateRoute(UpdateRoute dto, Long routeId, Long userId);

    ResponseWrapperRoutes getAllRoutes(Integer pageNumber, Integer pageSize, Long userId);

    void deleteRoute(Long routeId, Long userId);
}
