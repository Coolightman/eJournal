package com.coolightman.app.dto.response;

import lombok.Data;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponseDto {

    @NonNull
    private HttpStatus httpStatus;

    @NonNull
    private String message;
}
