package com.github.alekseypetkun.TransportTicketApp.service.impl;

import com.github.alekseypetkun.TransportTicketApp.constant.Role;
import com.github.alekseypetkun.TransportTicketApp.dao.impl.RouteDaoImpl;
import com.github.alekseypetkun.TransportTicketApp.dto.CarrierDto;
import com.github.alekseypetkun.TransportTicketApp.dto.CreateRoute;
import com.github.alekseypetkun.TransportTicketApp.dto.RouteDto;
import com.github.alekseypetkun.TransportTicketApp.mapper.RouteMapper;
import com.github.alekseypetkun.TransportTicketApp.model.Carrier;
import com.github.alekseypetkun.TransportTicketApp.model.Route;
import com.github.alekseypetkun.TransportTicketApp.model.User;
import com.github.alekseypetkun.TransportTicketApp.service.AuthService;
import com.github.alekseypetkun.TransportTicketApp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouteServiceImplTest {

    @Mock
    private RouteDaoImpl routeDao;

    @Mock
    private RouteMapper routeMapper;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @InjectMocks
    private RouteServiceImpl service;

    @Test
    void createRoute() {

        // Создаем тестовые данные для сущности Route
        Long userId = 1L;
        CreateRoute createRouteDto = new CreateRoute();
        createRouteDto.setDeparture("New York");
        createRouteDto.setDestination("Los Angeles");
        createRouteDto.setDurations_min(100);
        createRouteDto.setCarrier(1L);

        // Создаем тестовые данные для сущности User
        User admin = new User();
        admin.setId(userId);
        admin.setRole(Role.ADMIN);

        // Определяем результат, который вернет метод userService.findUserById()
        when(userService.findUserById(userId)).thenReturn(admin);
        // Определяем результат, который вернет метод authService.checkAccess()
        when(authService.checkAccess(admin)).thenReturn(true);

        // Создаем тестовые данные для сущности Route
        Route route = new Route();
        route.setDeparture("New York");
        route.setDestination("Los Angeles");
        route.setDurations_min(100);
        route.setCarrier(new Carrier());

        RouteDto expectedDto = new RouteDto(1L, "New York", "Los Angeles", new CarrierDto(), 100);

        // Устанавливаем поведение для mock-объекта routeDao
        when(routeDao.addEntity(route)).thenReturn(route);

        // Устанавливаем поведение для mock-объекта routeMapper
        when(routeMapper.map(createRouteDto)).thenReturn(route);
        when(routeMapper.map(route)).thenReturn(expectedDto);

        // Вызываем метод createRoute() и проверяем, что возвращенный объект RouteDto имеет правильные значения
        RouteDto result = service.createRoute(createRouteDto, userId);

        assertEquals(expectedDto.getDeparture(), result.getDeparture());
        assertEquals(expectedDto.getDestination(), result.getDestination());
        assertEquals(expectedDto.getDurations_min(), result.getDurations_min());
        assertEquals(expectedDto.getCarrier().getId(), result.getCarrier().getId());
        assertEquals(expectedDto, result);
    }

    @Test
    void updateRoute() {


    }

    @Test
    void getAllRoutes() {
    }

    @Test
    void deleteRoute() {
    }

    @Test
    void findRouteById() {
    }
}