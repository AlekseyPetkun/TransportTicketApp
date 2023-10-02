package com.github.alekseypetkun.TransportTicketApp.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO регистрация
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterRequest {

    @Schema(description = "имя пользователя")
    @NotEmpty(message = "строка с именем не может быть пустой!")
    @Size(min = 2, max = 30)
    private String firstName;

    @Schema(description = "фамилия пользователя")
    @NotEmpty(message = "строка с фамилией не может быть пустой!")
    @Size(min = 2, max = 30)
    private String lastName;

    @Schema(description = "email пользователя")
    @NotEmpty(message = "строка с email не может быть пустой!")
    @Email
    private String email;

    @Schema(description = "пароль пользователя")
    @NotEmpty(message = "строка с паролем не может быть пустой!")
    @Size(min = 8, max = 30)
    private String password;
}
