package com.github.alekseypetkun.TransportTicketApp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.alekseypetkun.TransportTicketApp.constant.Reserved;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO изменение билета
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateTicket {

    @Schema(description = "информация о маршруте")
    private Long route;

    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    @Schema(description = "дата и время отправления, вводить в формате: дд.мм.гггг чч:мм")
    private LocalDateTime departureDateTime;

    @Schema(description = "номер места")
    @Positive
    private Integer placeNumber;

    @Schema(description = "стоимость билета")
    @Positive
    private Integer price;

    @Schema(description = "резерв места")
    private Reserved reserved;
}
