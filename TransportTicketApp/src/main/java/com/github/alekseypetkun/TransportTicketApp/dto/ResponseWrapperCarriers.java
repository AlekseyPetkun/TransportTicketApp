package com.github.alekseypetkun.TransportTicketApp.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ДТО всех перевозчиков
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseWrapperCarriers {

    @Schema(description = "общее количество перевозчиков")
    private Long count;

    @Schema(description = "все найденные перевозчики")
    private List<CarrierDto> results;
}
