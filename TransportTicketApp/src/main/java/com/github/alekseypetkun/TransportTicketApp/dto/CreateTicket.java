package com.github.alekseypetkun.TransportTicketApp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO создание билета
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateTicket {

    @Schema(description = "идентификатор маршрута")
    @NotNull(message = "строка с routeId не может быть пустой!")
    private Long routeId;

    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    @Schema(description = "дата и время отправления, вводить в формате: дд.мм.гггг чч:мм")
    @NotNull(message = "строка с датой и временем не может быть пустой!")
//    @Pattern(regexp = "^(0[1-9]|[1-2][0-9]|3[0-1]).(0[1-9]|1[0-2]).(19|20)\\d{2} (0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")
    private LocalDateTime departureDateTime;

    @Schema(description = "номер места")
    @NotNull(message = "строка с номером места не может быть пустой!")
    @Positive
    private Integer placeNumber;

    @Schema(description = "стоимость билета")
    @NotNull(message = "строка со стоимостью билета не может быть пустой!")
    @Positive
    private Integer price;
}
