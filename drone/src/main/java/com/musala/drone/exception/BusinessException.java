package com.musala.drone.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class BusinessException extends RuntimeException {
    private HttpStatus status;
    private String message;

}
