package com.armdoctor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(DoctorValidationException.class)
    public ResponseEntity<ErrorResponse> validationException(DoctorValidationException exception){
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(),HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundExceptionHandler(DoctorNotFoundException exception){
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(),HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> ResourceAlreadyExistExceptionHandler(ResourceAlreadyExistException exception){
     ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(),HttpStatus.CONFLICT.value());
     return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> apiExceptionHendler(ApiException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
