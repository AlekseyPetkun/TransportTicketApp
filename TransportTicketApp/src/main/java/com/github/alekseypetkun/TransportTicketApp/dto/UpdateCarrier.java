package com.github.alekseypetkun.TransportTicketApp.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
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
public class UpdateCarrier {

    @Schema(description = "название компании, должно быть уникальным")
    private String name;

    @Schema(description = "номер телефона компании, должен быть уникальным, в формате: +79991234567")
    @Pattern(regexp = "^[+][7][0-9]{10}$")
    private String phone;
}
