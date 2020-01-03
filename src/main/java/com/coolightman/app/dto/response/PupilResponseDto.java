package com.coolightman.app.dto.response;

import com.coolightman.app.model.AClass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class PupilResponseDto extends UserResponseDto {

    private String firstName;

    private String surname;

    private String dob;

    private AClass aClass;

    public void setDob(final LocalDate dob) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.dob = formatter.format(dob);
    }
}