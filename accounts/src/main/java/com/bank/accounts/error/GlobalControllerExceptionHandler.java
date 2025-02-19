package com.bank.accounts.error;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ApiErrorDto> handleEntityExistException(EntityExistsException ex) {
        return buildErrorEntity(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleEntityNotFoundException(EntityNotFoundException ex) {
        return buildErrorEntity(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiErrorDto> handleNullPointerException(NullPointerException ex) {
        return buildErrorEntity(HttpStatus.BAD_REQUEST, ex);
    }

    private ResponseEntity<ApiErrorDto> buildErrorEntity(HttpStatus httpStatus, Exception ex) {
        return new ResponseEntity<>(new ApiErrorDto(httpStatus.toString(), ex.getMessage()), httpStatus);
    }

    private ResponseEntity<ApiErrorDto> buildErrorEntity(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new ApiErrorDto(httpStatus.toString(), message), httpStatus);
    }

}
