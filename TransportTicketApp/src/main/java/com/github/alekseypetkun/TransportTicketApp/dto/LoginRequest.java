package com.github.alekseypetkun.TransportTicketApp.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO вход в систему
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoginRequest {

    @Schema(description = "логин")
    @NotEmpty(message = "строка с логином не может быть пустой!")
    @Size(min = 2, max = 30)
    private String username;

    @Schema(description = "пароль")
    @NotEmpty(message = "строка с паролем не может быть пустой!")
//    @Size(min = 8, max = 30)
    private String password;
}
