package com.coolightman.app.dto.response;

import lombok.Getter;

@Getter
public class JwtResponseDto {
    private final String token;

    public JwtResponseDto(final String token) {
        this.token = token;
    }
}
