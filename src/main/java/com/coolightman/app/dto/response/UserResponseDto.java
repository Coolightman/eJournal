package com.coolightman.app.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class UserResponseDto {

    private Long id;

    private String login;
}
