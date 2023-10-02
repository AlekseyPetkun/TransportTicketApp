package com.github.alekseypetkun.TransportTicketApp.security.jwt;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshJwtRequest {

    @NotEmpty(message = "строка с токеном не может быть пустой!")
    public String refreshToken;

}
