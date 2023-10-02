package com.github.alekseypetkun.TransportTicketApp.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * DTO добавление маршрута
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateRoute {

    @Schema(description = "пункт отправления")
    @NotEmpty(message = "строка с отправлением не может быть пустой!")
    private String departure;

    @Schema(description = "пункт назначения")
    @NotEmpty(message = "строка с назначением не может быть пустой!")
    private String destination;

    @Schema(description = "перевозчик")
    @NotNull(message = "строка с перевозчиком не может быть пустой!")
    private Long carrier;

    @Schema(description = "время в пути (в минутах)")
    @NotNull(message = "строка с временем не может быть пустой!")
    private Integer durations_min;
}
