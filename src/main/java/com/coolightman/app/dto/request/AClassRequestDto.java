package com.coolightman.app.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AClassRequestDto {

    @NotNull(message = "{aclass.name.notNull}")
    @NotEmpty(message = "{aclass.name.notEmpty}")
    @Size(min = 2, max = 6, message = "{class.name.size}")
    private String name;
}
