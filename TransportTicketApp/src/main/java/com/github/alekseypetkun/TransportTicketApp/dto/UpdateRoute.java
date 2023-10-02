package com.github.alekseypetkun.TransportTicketApp.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * DTO изменение перевозчика
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateRoute {

    @Schema(description = "пункт отправления")
    private String departure;

    @Schema(description = "пункт назначения")
    private String destination;

    @Schema(description = "перевозчик")
    private Long carrier;

    @Schema(description = "время в пути (в минутах)")
    @Positive
    private Integer durations_min;
}
