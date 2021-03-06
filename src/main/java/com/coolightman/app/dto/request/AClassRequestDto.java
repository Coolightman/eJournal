package com.coolightman.app.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The type A class request dto.
 */
@Getter
@Setter
public class AClassRequestDto {

    private Long id;

    @NotEmpty
    @NotNull(message = "{aclass.name.notNull}")
    @Size(min = 2, max = 6, message = "{class.name.size}")
    private String name;
}
