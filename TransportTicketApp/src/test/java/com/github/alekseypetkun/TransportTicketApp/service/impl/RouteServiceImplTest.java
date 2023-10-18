package com.github.alekseypetkun.TransportTicketApp.service.impl;

import com.github.alekseypetkun.TransportTicketApp.constant.Role;
import com.github.alekseypetkun.TransportTicketApp.dao.impl.RouteDaoImpl;
import com.github.alekseypetkun.TransportTicketApp.dto.*;
import com.github.alekseypetkun.TransportTicketApp.exception.AuthorisationException;
import com.github.alekseypetkun.TransportTicketApp.exception.NotFoundCarrierException;
import com.github.alekseypetkun.TransportTicketApp.exception.NotFoundRouteException;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

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
    private RouteServiceImpl routeService;

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
        RouteDto result = routeService.createRoute(createRouteDto, userId);

        assertEquals(expectedDto.getDeparture(), result.getDeparture());
        assertEquals(expectedDto.getDestination(), result.getDestination());
        assertEquals(expectedDto.getDurations_min(), result.getDurations_min());
        assertEquals(expectedDto.getCarrier().getId(), result.getCarrier().getId());
        assertEquals(expectedDto, result);
    }

    @Test
    void updateRoute() {

        // Создаем тестовые данные для пользователя с правами доступа
        Long userId = 1L;
        User admin = new User();
        admin.setId(userId);
        admin.setRole(Role.ADMIN);

        // Определяем результат, который вернет метод userService.findUserById()
        when(userService.findUserById(userId)).thenReturn(admin);
        // Определяем результат, который вернет метод authService.checkAccess()
        when(authService.checkAccess(admin)).thenReturn(true);

        // Создаем тестовые данные для сущности Carrier
        Long carrierId = 1L;

        Long routeId = 1L;
        Route route = new Route();
        route.setDurations_min(50);
        route.setDeparture("Test1");
        route.setDestination("Test2");
        route.setId(routeId);

        // Создаем тестовые данные для ДТО запроса
        UpdateRoute updateDto = new UpdateRoute("New York", "Los Angeles", carrierId, 100);

        // Определяем ожидаемый результат
        RouteDto expectedDto = new RouteDto(1L, "New York", "Los Angeles", new CarrierDto(), 100);

        // Определяем результат, который вернет метод routeService.updateRoute()
        when(routeDao.searchById(routeId)).thenReturn(route);
        doNothing().when(routeDao).updateEntity(route);
        when(routeMapper.map(route)).thenReturn(expectedDto);

        // Вызываем метод routeService.updateRoute()
        RouteDto result = routeService.updateRoute(updateDto, routeId, userId);

        // и проверяем результат
        assertNotNull(result);
        assertEquals("New York", result.getDeparture());
        assertEquals("Los Angeles", result.getDestination());
        assertEquals(100, result.getDurations_min());
        verify(routeDao, times(1)).updateEntity(route);
    }

    @Test
    void getAllRoutes() {

        // Создаем тестовые данные для пользователя с правами доступа
        Long userId = 1L;
        User admin = new User();
        admin.setId(userId);
        admin.setRole(Role.ADMIN);

        // Определяем результат, который вернет метод userService.findUserById()
        when(userService.findUserById(userId)).thenReturn(admin);
        // Определяем результат, который вернет метод authService.checkAccess()
        when(authService.checkAccess(admin)).thenReturn(true);

        // Создаем тестовые данные для списка перевозчиков
        Long totalAmount = 100L;
        Integer pageNumber = 1;
        Integer pageSize = 10;

        List<Route> entityList = new ArrayList<>();
        entityList.add(new Route());
        entityList.add(new Route());
        entityList.add(new Route());

        // Определяем результат, который вернет метод routeService.getAllRoutes()
        when(routeDao.count()).thenReturn(totalAmount);
        when(routeDao.returnAll(pageNumber, pageSize)).thenReturn(entityList);
        when(routeMapper.map(any(Route.class))).thenReturn(
                new RouteDto(),
                new RouteDto(),
                new RouteDto());

        // Вызываем метод routeService.getAllRoutes()
        ResponseWrapperRoutes result = routeService.getAllRoutes(pageNumber, pageSize, userId);

        // и проверяем результат
        assertNotNull(result);
        assertEquals(totalAmount, result.getCount());
        assertEquals(3, result.getResults().size());
    }

    @Test
    void deleteRoute() {

        // Создаем тестовые данные для пользователя с правами доступа
        Long userId = 1L;
        User admin = new User();
        admin.setId(userId);
        admin.setRole(Role.ADMIN);

        // Определяем результат, который вернет метод userService.findUserById()
        when(userService.findUserById(userId)).thenReturn(admin);
        // Определяем результат, который вернет метод authService.checkAccess()
        when(authService.checkAccess(admin)).thenReturn(true);

        // Создаем тестовые данные для сущности Route
        Long routeId = 1L;
        Route route = new Route();
        route.setId(routeId);

        // Определяем результат, который вернет метод routeService.deleteRoute()
        when(routeDao.searchById(anyLong())).thenReturn(route);
        doNothing().when(routeDao).deleteEntityById(routeId);

        // Вызываем метод routeService.deleteRoute()
        routeService.deleteRoute(route.getId(), userId);

        // и проверяем результат
        verify(routeDao, times(1)).deleteEntityById(route.getId());
    }

    @Test
    void findRouteById() {

        // Создаем тестовые данные для сущности Route
        Long routeId = 1L;
        Route route = new Route();
        route.setId(routeId);
        when(routeDao.searchById(routeId)).thenReturn(route);

        // Вызываем метод routeService.findRouteById()
        Route result = routeService.findRouteById(routeId);

        // и проверяем результат
        assertEquals(routeId, result.getId());
    }

    @Test
    public void testFindCarrierByIdNotFound() {

        // Создаем тестовые данные для сущности Route
        Long routeId = 2L;
        when(routeDao.searchById(routeId)).thenReturn(null);

        // и проверяем результат
        assertThrows(NotFoundRouteException.class, () ->
                routeService.findRouteById(routeId));
    }

    @Test
    public void shouldThrowAuthorisationException() {

        // Создаем тестовые данные для пользователя без прав доступа
        Long userId = 1L;
        User notAdmin = new User();
        notAdmin.setId(userId);
        notAdmin.setRole(Role.USER);

        // Определяем результат, который вернет метод userService.findUserById()
        when(userService.findUserById(userId)).thenReturn(notAdmin);
        // Определяем результат, который вернет метод authService.checkAccess()
        when(authService.checkAccess(notAdmin)).thenReturn(false);

        assertThrows(AuthorisationException.class, () ->
                routeService.createRoute(any(CreateRoute.class), userId));

        assertThrows(AuthorisationException.class, () ->
                routeService.updateRoute(mock(UpdateRoute.class), anyLong(), userId));

//        assertThrows(AuthorisationException.class, () ->
//                routeService.getAllRoutes(anyInt(), anyInt(), userId));

        assertThrows(AuthorisationException.class, () ->
                routeService.deleteRoute(anyLong(), userId));
    }
}