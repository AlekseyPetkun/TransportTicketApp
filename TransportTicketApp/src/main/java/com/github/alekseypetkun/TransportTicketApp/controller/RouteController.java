package com.github.alekseypetkun.TransportTicketApp.controller;

import com.github.alekseypetkun.TransportTicketApp.dto.*;
import com.github.alekseypetkun.TransportTicketApp.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для работы с маршрутами
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routes")
@Tag(name = "API по работе с маршрутами")
public class RouteController {

    private final RouteService routeService;
    private final UserService userService;

    @PostMapping
    @Operation(
            summary = "Создание нового маршрута",
            description = "Нужно заполнить параметры для создания",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Маршрут добавлен (Created)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = RouteDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Маршрут не добавлен, " +
                                    "т.к. не прошел валидацию (Bad Request)"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка, не зависящая от вызывающей стороны"
                    )
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public RouteDto createCarrier(@RequestBody @Valid CreateRoute dto) {

        Long userId = userService.getAuthenticatedUserId();
        return routeService.createRoute(dto, userId);
    }

    @PatchMapping("/{routeId}")
    @Operation(
            summary = "Обновить информацию о маршруте по его идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация успешно обновилась (Ok)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = RouteDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Информация не обновилась, " +
                                    "т.к. пользователь не авторизован (Unauthorized)"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Информация не обновилась, " +
                                    "т.к. у пользователя нет прав на это (Forbidden)"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка, не зависящая от вызывающей стороны"
                    )
            }
    )
    public RouteDto updateRoute(@RequestBody @Valid UpdateRoute dto,
                                @PathVariable("routeId") Long routeId) {

        Long userId = userService.getAuthenticatedUserId();
        return routeService.updateRoute(dto, routeId, userId);
    }

    @GetMapping
    @Operation(
            summary = "Получить все маршруты",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получен список маршрутов (Ok)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseWrapperRoutes.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Список маршрутов не получен (Not Found)"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка, не зависящая от вызывающей стороны"
                    )
            }
    )
    public ResponseWrapperRoutes getAllRoutes(
            @RequestParam(required = false, value = "page_number", defaultValue = "0") @Min(0) Integer pageSize,
            @RequestParam(required = false, value = "page_size", defaultValue = "10") Integer pageNumber) {

        Long userId = userService.getAuthenticatedUserId();
        return routeService.getAllRoutes(pageNumber, pageSize, userId);
    }

    @DeleteMapping("/{routeId}")
    @Operation(
            summary = "Удалить маршрут по его идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Маршрут удален (No Content)"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Маршрут не удален, " +
                                    "т.к. пользователь не авторизован (Unauthorized)"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Маршрут не удален, " +
                                    "т.к. у пользователя нет прав на это (Forbidden)"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка, не зависящая от вызывающей стороны"
                    )
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoute(@PathVariable("routeId") Long routeId) {

        Long userId = userService.getAuthenticatedUserId();
        routeService.deleteRoute(routeId, userId);
    }
}
