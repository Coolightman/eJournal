package com.coolightman.app.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class PupilRequestDto extends UserRequestDto {

    @NotNull(message = "{pupil.firstName.notNull}")
    @NotEmpty(message = "{pupil.firstName.notEmpty}")
    @Size(min = 2, max = 80, message = "{pupil.firstName.size}")
    private String firstName;

    @NotNull(message = "{pupil.surname.notNull}")
    @NotEmpty(message = "{pupil.surname.notEmpty}")
    @Size(min = 2, max = 80, message = "{pupil.surname.size}")
    private String surname;

    @NotNull(message = "{pupil.dob.notNull}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;

    @NotNull(message = "{pupil.aClass.notNull}")
    private Long aClassId;
}
