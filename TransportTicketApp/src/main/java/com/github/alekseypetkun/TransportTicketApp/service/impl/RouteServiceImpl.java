package com.github.alekseypetkun.TransportTicketApp.service.impl;

import com.github.alekseypetkun.TransportTicketApp.dao.impl.RouteDaoImpl;
import com.github.alekseypetkun.TransportTicketApp.dto.*;
import com.github.alekseypetkun.TransportTicketApp.exception.*;
import com.github.alekseypetkun.TransportTicketApp.mapper.RouteMapper;
import com.github.alekseypetkun.TransportTicketApp.model.*;
import com.github.alekseypetkun.TransportTicketApp.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Бизнес-логика по работе с маршрутами
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final RouteDaoImpl routeDao;
    private final RouteMapper routeMapper;
    private final AuthService authService;
    private final UserService userService;

    @Override
    public RouteDto createRoute(CreateRoute dto, Long userId) {

        User admin = userService.findUserById(userId);

        if (authService.checkAccess(admin)) {

            Route route = routeMapper.map(dto);

            RouteDto newRoute = routeMapper.map(routeDao.addEntity(route));
            log.info("IN createRoute - route: {} created", route);
            return newRoute;
        } else {
            throw new AuthorisationException("user is forbidden", "8070_USER_IS_FORBIDDEN");
        }
    }

    @Override
    public RouteDto updateRoute(UpdateRoute dto, Long routeId, Long userId) {

        User admin = userService.findUserById(userId);

        if (authService.checkAccess(admin)) {

            Route route = findRouteById(routeId);

            routeMapper.patch(dto, route);

            routeDao.updateEntity(route);
            RouteDto updatedRoute = routeMapper.map(route);

            log.info("IN updateRoute - route: {} updated", route);
            return updatedRoute;
        } else {
            throw new AuthorisationException("user is forbidden", "8070_USER_IS_FORBIDDEN");
        }
    }

    @Override
    public ResponseWrapperRoutes getAllRoutes(Integer pageNumber, Integer pageSize, Long userId) {

        User admin = userService.findUserById(userId);

        if (authService.checkAccess(admin)) {

            Long totalAmount = routeDao.count();
            List<RouteDto> dtoList = new ArrayList<>(routeDao
                    .returnAll(pageNumber, pageSize)).stream()
                    .map(routeMapper::map)
                    .toList();

            return new ResponseWrapperRoutes(totalAmount, dtoList);
        } else {
            throw new AuthorisationException("user is forbidden", "8070_USER_IS_FORBIDDEN");
        }
    }

    @Override
    public void deleteRoute(Long routeId, Long userId) {

        User admin = userService.findUserById(userId);

        if (authService.checkAccess(admin)) {

            Route route = findRouteById(routeId);

            routeDao.deleteEntityById(routeId);
            log.info("IN deleteRoute - route: {} deleted", route);
        } else {
            throw new AuthorisationException("user is forbidden", "8070_USER_IS_FORBIDDEN");
        }
    }

    @Override
    public Route findRouteById(Long routeId) {

        Route route = routeDao.searchById(routeId);
        if (route == null) {
            throw new NotFoundRouteException(routeId);
        }
        return route;
    }
}
