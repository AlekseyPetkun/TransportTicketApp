package com.example.ConsumerForTicketService.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * DTO информация о маршруте
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RouteDto {

    private Long id;

    @Schema(description = "пункт отправления")
    private String departure;

    @Schema(description = "пункт назначения")
    private String destination;

    @Schema(description = "перевозчик")
    private CarrierDto carrier;

    @Schema(description = "время в пути (в минутах)")
    private Integer durations_min;
}
