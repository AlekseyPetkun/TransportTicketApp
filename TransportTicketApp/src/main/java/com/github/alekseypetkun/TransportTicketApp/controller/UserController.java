package com.github.alekseypetkun.TransportTicketApp.controller;

import com.github.alekseypetkun.TransportTicketApp.dto.*;
import com.github.alekseypetkun.TransportTicketApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для работы с пользователями
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "API по работе с пользователями")
public class UserController {

    private final UserService userService;

    @PatchMapping("/me")
    @Operation(
            summary = "Обновить информацию о пользователе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация успешно обновилась (Ok)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Информация не обновилась, " +
                                    "т.к. пользователь не авторизован (Unauthorized)"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Информация не обновилась, " +
                                    "т.к. у пользователя нет прав на это (Forbidden)"
                    )
            }
    )
    public UserDto updateUser(@RequestBody @Valid UpdateUserDto dto) {

        Long userId = userService.getAuthenticatedUserId();
        return userService.updateUser(dto, userId);
    }

    @GetMapping()
    @Operation(
            summary = "Получить всех пользователей",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получен список пользователей (Ok)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseWrapperUsers.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Список пользователей не получен (Not Found)"
                    )
            }
    )
    public ResponseWrapperUsers getAllUsers(
            @RequestParam(required = false, value = "page_number", defaultValue = "0") @Min(0) Integer pageSize,
            @RequestParam(required = false, value = "page_size", defaultValue = "10") Integer pageNumber) {

        return userService.getAllUsers(pageNumber, pageSize);
    }

    @DeleteMapping("/delete")
    @Operation(
            summary = "Удалить пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Пользователь удален (No Content)"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не удален, " +
                                    "т.к. пользователь не авторизован (Unauthorized)"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Пользователь не удален, " +
                                    "т.к. у пользователя нет прав на это (Forbidden)"
                    )
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser() {

        Long userId = userService.getAuthenticatedUserId();
        userService.deleteUser(userId);
    }
}
