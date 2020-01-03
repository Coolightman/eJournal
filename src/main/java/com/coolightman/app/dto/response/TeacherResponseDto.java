package com.coolightman.app.dto.response;

import com.coolightman.app.model.Discipline;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeacherResponseDto extends UserResponseDto {

    private String firstName;
    private String surname;
    private List<Discipline> disciplines;
}
