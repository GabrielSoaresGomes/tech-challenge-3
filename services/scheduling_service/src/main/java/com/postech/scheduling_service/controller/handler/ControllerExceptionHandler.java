package com.postech.scheduling_service.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler({EntityNotFoundException.class, JpaObjectRetrievalFailureException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleEntityNotFound(Exception ex) {
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
