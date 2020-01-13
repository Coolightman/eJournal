package com.coolightman.app.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * The type User response dto.
 */
@Getter
@Setter
public abstract class UserResponseDto {

    private Long id;

    private String login;
}
