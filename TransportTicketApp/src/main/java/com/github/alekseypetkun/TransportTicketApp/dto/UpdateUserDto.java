package com.github.alekseypetkun.TransportTicketApp.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * DTO изменение информации о пользователе
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateUserDto {

    @Schema(description = "логин пользователя")
    @Size(min = 2, max = 30)
    private String username;

    @Schema(description = "почта пользователя")
    @Email
    private String email;

    @Schema(description = "имя пользователя")
    @Size(min = 2, max = 30)
    private String firstName;

    @Schema(description = "фамилия пользователя")
    @Size(min = 2, max = 30)
    private String lastName;
}
