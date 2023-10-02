package com.github.alekseypetkun.TransportTicketApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.alekseypetkun.TransportTicketApp.constant.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO информация о пользователе
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDto {

    private Long id;

    @Schema(description = "логин пользователя")
    private String username;

    // поле будет записано только как часть десериализации,
    // но значение поля не включается в сериализацию
    @Schema(description = "пароль пользователя")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Schema(description = "почта пользователя")
    private String email;

    @Schema(description = "роль пользователя")
    private Role role;

    @Schema(description = "имя пользователя")
    private String firstName;

    @Schema(description = "фамилия пользователя")
    private String lastName;

    @Schema(description = "доступ пользователя")
    private boolean enabled;

    @Schema(description = "дата регистрации пользователя")
    private LocalDateTime createdAt;

    @Schema(description = "дата изменения информации о пользователе")
    private LocalDateTime updatedAt;
}
