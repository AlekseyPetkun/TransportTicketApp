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
 * Контроллер для работы с покупкой билетов
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
@Tag(name = "API по работе с покупкой билетов")
public class TicketController {

    private final UserService userService;
    private final TicketService ticketService;

    @PostMapping
    @Operation(
            summary = "Создание нового билета",
            description = "Нужно заполнить параметры для создания",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Билет добавлен (Created)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TicketDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Билет не добавлен, " +
                                    "т.к. не прошел валидацию (Bad Request)"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка, не зависящая от вызывающей стороны"
                    )
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public TicketDto createCarrier(@RequestBody @Valid CreateTicket dto) {

        Long userId = userService.getAuthenticatedUserId();
        return ticketService.createTicket(dto, userId);
    }

    @PatchMapping("/buy/{ticketId}")
    @Operation(
            summary = "Купить билет по его идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Билет успешно куплен (Ok)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BuyerTicketDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Билет не куплен, " +
                                    "т.к. пользователь не авторизован (Unauthorized)"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Билет не куплен, " +
                                    "т.к. у пользователя нет прав на это (Forbidden)"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка, не зависящая от вызывающей стороны"
                    )
            }
    )
    public BuyerTicketDto buyTicket(@PathVariable("ticketId") Long ticketId) {

        Long userId = userService.getAuthenticatedUserId();
        return ticketService.buyTicket(ticketId, userId);
    }

    @PatchMapping("/cansel/{ticketId}")
    @Operation(
            summary = "Вернуть билет по его идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Возврат успешно оформлен (No Content)"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Возврат не оформлен, " +
                                    "т.к. пользователь не авторизован (Unauthorized)"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Возврат не оформлен, " +
                                    "т.к. у пользователя нет прав на это (Forbidden)"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка, не зависящая от вызывающей стороны"
                    )
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void canselBuyTicket(@PathVariable("ticketId") Long ticketId) {

        Long userId = userService.getAuthenticatedUserId();
        ticketService.canselBuyTicket(ticketId, userId);
    }

    @PatchMapping("/{ticketId}")
    @Operation(
            summary = "Обновить информацию о билете по его идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация успешно обновилась (Ok)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TicketDto.class)
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
    public TicketDto updateTicket(@PathVariable("ticketId") Long ticketId,
                                  @RequestBody @Valid UpdateTicket dto) {

        Long userId = userService.getAuthenticatedUserId();
        return ticketService.updateTicket(dto, ticketId, userId);
    }

    @GetMapping("/me")
    @Operation(
            summary = "Получить купленные билеты авторизованного пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация успешно получена (Ok)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseWrapperTickets.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Информация не получена, " +
                                    "т.к. пользователь не авторизован (Unauthorized)"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Информация не обновилась, " +
                                    "т.к. у пользователя нет прав на это (Forbidden)"
                    )
            }
    )
    public ResponseWrapperBuyerTickets getAllMeTickets(
            @RequestParam(required = false, value = "page_number", defaultValue = "0") @Min(0) Integer pageSize,
            @RequestParam(required = false, value = "page_size", defaultValue = "10") Integer pageNumber) {

        Long userId = userService.getAuthenticatedUserId();
        return ticketService.getAllMeTickets(userId, pageNumber, pageSize);
    }

    @GetMapping
    @Operation(
            summary = "Получить все свободные билеты",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получен список свободных билетов (Ok)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseWrapperTickets.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Список билетов не получен (Not Found)"
                    )
            }
    )
    public ResponseWrapperTickets getAllTickets(
            @RequestParam(required = false, value = "page_number", defaultValue = "0") @Min(0) Integer pageSize,
            @RequestParam(required = false, value = "page_size", defaultValue = "10") Integer pageNumber) {

        return ticketService.getAllTickets(pageNumber, pageSize);
    }

    @DeleteMapping("/{ticketId}")
    @Operation(
            summary = "Удалить билет по его идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Билет удален (No Content)"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Билет не удален, " +
                                    "т.к. пользователь не авторизован (Unauthorized)"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Билет не удален, " +
                                    "т.к. у пользователя нет прав на это (Forbidden)"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка, не зависящая от вызывающей стороны"
                    )
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTicket(@PathVariable("ticketId") Long ticketId) {

        Long userId = userService.getAuthenticatedUserId();
        ticketService.deleteTicket(ticketId, userId);
    }
}
