package com.coolightman.app.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * The type Grade request dto.
 */
@Getter
@Setter
public class GradeRequestDto {

    @NotNull(message = "{grade.value.notNull}")
    @Range(min = 1, max = 10, message = "{grade.value.range}")
    private Short value;

    @NotNull(message = "{grade.date.notNull}")
    private LocalDate date;

    @NotNull(message = "{grade.discipline.notNull}")
    private Long disciplineId;

    @NotNull(message = "{grade.pupil.notNull}")
    private Long pupilId;
}
