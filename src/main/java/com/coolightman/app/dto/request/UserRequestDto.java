package com.coolightman.app.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class UserRequestDto {

    @NotNull(message = "{user.login.notNull}")
    @Size(min = 5, max = 20, message = "{user.login.size}")
    private String login;

    @NotNull(message = "{user.password.notNull}")
    @Size(min = 5, max = 10, message = "{user.password.size}")
    private String password;
}
