package com.armdoctor.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor

public class ErrorResponse {
    private String message;
    private int errorCode;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
