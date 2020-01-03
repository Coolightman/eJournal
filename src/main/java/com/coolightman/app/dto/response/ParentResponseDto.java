package com.coolightman.app.dto.response;

import com.coolightman.app.model.Pupil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParentResponseDto extends UserResponseDto {

    private Pupil pupil;
}
