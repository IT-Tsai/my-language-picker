package io.practice.programming_languages_picker.handler;

import io.practice.programming_languages_picker.model.ServerError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNoHandlerFoundException(NoHandlerFoundException ex) {
            return new ResponseEntity<>(new ServerError("Endpoint not found: " + ex.getRequestURL(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }

}
