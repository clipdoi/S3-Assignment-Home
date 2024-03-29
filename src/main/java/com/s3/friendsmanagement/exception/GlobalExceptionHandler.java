package com.s3.friendsmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InputInvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage inputInvalidException(InputInvalidException ex, WebRequest webRequest) {
        return new ErrorMessage(HttpStatus.BAD_REQUEST.value()
                , ex.getMessage()
                , LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
                , webRequest.getDescription(false));
    }

    @ExceptionHandler(StatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage statusException(StatusException ex, WebRequest webRequest) {
        return new ErrorMessage(HttpStatus.BAD_REQUEST.value()
                , ex.getMessage()
                , LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
                , webRequest.getDescription(false));
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage dataNotFoundException(DataNotFoundException ex, WebRequest webRequest) {
        return new ErrorMessage(HttpStatus.NOT_FOUND.value()
                , ex.getMessage()
                , LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
                , webRequest.getDescription(false));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleRoleNotFoundException(RoleNotFoundException ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),
                request.getDescription(false));
    }
}
