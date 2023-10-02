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
 * Контроллер для работы с перевозчиками
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carriers")
@Tag(name = "API по работе с перевозчиками")
public class CarrierController {

    private final CarrierService carrierService;
    private final UserService userService;

    @PostMapping
    @Operation(
            summary = "Регистрация нового перевозчика",
            description = "Нужно заполнить параметры для регистрации",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Перевозчик зарегистрирован (Created)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CarrierDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Перевозчик не зарегистрирован, " +
                                    "т.к. не прошел валидацию (Bad Request)"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка, не зависящая от вызывающей стороны"
                    )
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public CarrierDto createCarrier(@RequestBody @Valid CreateCarrier dto) {

        Long userId = userService.getAuthenticatedUserId();
        return carrierService.createCarrier(dto, userId);
    }

    @PatchMapping("/{carrierId}")
    @Operation(
            summary = "Обновить информацию о перевозчике по его идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация успешно обновилась (Ok)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CarrierDto.class)
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
    public CarrierDto updateCarrier(@RequestBody @Valid UpdateCarrier dto,
                                    @PathVariable("carrierId") Long carrierId) {

        Long userId = userService.getAuthenticatedUserId();
        return carrierService.updateCarrier(dto, carrierId, userId);
    }

    @GetMapping
    @Operation(
            summary = "Получить всех перевозчиков",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получен список перевозчиков (Ok)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseWrapperCarriers.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Список перевозчиков не получен (Not Found)"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка, не зависящая от вызывающей стороны"
                    )
            }
    )
    public ResponseWrapperCarriers getAllCarriers(
            @RequestParam(required = false, value = "page_number", defaultValue = "0") @Min(0) Integer pageSize,
            @RequestParam(required = false, value = "page_size", defaultValue = "10") Integer pageNumber) {

        Long userId = userService.getAuthenticatedUserId();
        return carrierService.getAllCarriers(pageNumber, pageSize, userId);
    }

    @DeleteMapping("/{carrierId}")
    @Operation(
            summary = "Удалить перевозчика по его идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Перевозчик удален (No Content)"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Перевозчик не удален, " +
                                    "т.к. пользователь не авторизован (Unauthorized)"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Перевозчик не удален, " +
                                    "т.к. у пользователя нет прав на это (Forbidden)"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Произошла ошибка, не зависящая от вызывающей стороны"
                    )
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCarrier(@PathVariable("carrierId") Long carrierId) {

        Long userId = userService.getAuthenticatedUserId();
        carrierService.deleteCarrier(carrierId, userId);
    }
}
