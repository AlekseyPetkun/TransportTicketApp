package com.github.alekseypetkun.TransportTicketApp.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * DTO информация о перевозчике
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CarrierDto {

    private Long id;

    @Schema(description = "название компании, должно быть уникальным")
    private String name;

    @Schema(description = "номер телефона компании, должен быть уникальным")
    private String phone;
}
