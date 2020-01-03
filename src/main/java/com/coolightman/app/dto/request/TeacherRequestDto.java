package com.coolightman.app.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class TeacherRequestDto extends UserRequestDto {

    @NotNull(message = "{teacher.firstName.notNull}")
    @NotEmpty(message = "{teacher.firstName.notEmpty}")
    @Size(min = 2, max = 80, message = "{teacher.firstName.size}")
    private String firstName;

    @NotNull(message = "{teacher.surname.notNull}")
    @NotEmpty(message = "{teacher.surname.notEmpty}")
    @Size(min = 2, max = 80, message = "{teacher.surname.size}")
    private String surname;

    @NotNull(message = "{teacher.discipline.notNull}")
    private List<Long> disciplines;
}
