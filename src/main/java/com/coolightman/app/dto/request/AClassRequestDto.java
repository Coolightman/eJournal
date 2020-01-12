package com.coolightman.app.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AClassRequestDto {
    private Long id;

    @NotNull(message = "{aclass.name.notNull}")
    @Size(min = 2, max = 6, message = "{class.name.size}")
    private String name;
}
