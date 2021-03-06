package com.coolightman.app.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The type Discipline request dto.
 */
@Getter
@Setter
public class DisciplineRequestDto {

    private Long id;

    @NotNull(message = "{discipline.name.notNull}")
    @Size(min = 5, max = 80, message = "{discipline.name.size}")
    private String name;
}
