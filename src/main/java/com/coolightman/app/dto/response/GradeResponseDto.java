package com.coolightman.app.dto.response;

import com.coolightman.app.model.Discipline;
import com.coolightman.app.model.Pupil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class GradeResponseDto {

    private Long id;

    private Short value;

    private String date;

    private Discipline discipline;

    private Pupil pupil;

    public void setDate(final LocalDate dob) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.date = formatter.format(dob);
    }
}
