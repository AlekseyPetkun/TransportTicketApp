package com.example.ConsumerForTicketService.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO информация о билете пользователя
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BuyerTicketDto {

    private Long id;

    @Schema(description = "информация о маршруте")
    private RouteDto route;

    @Schema(description = "информация о покупателе")
    private BuyerDto buyer;

    @Schema(description = "дата и время отправления")
    private LocalDateTime departureDateTime;

    @Schema(description = "номер места")
    private Integer placeNumber;

    @Schema(description = "стоимость билета")
    private Integer price;
}
