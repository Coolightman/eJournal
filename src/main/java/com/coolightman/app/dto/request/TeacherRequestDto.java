package com.coolightman.app.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * The type Teacher request dto.
 */
@Getter
@Setter
public class TeacherRequestDto extends UserRequestDto {

    @NotNull(message = "{teacher.firstName.notNull}")
    @Size(min = 2, max = 80, message = "{teacher.firstName.size}")
    private String firstName;

    @NotNull(message = "{teacher.surname.notNull}")
    @Size(min = 2, max = 80, message = "{teacher.surname.size}")
    private String surname;

    private List<Long> disciplines;
}
