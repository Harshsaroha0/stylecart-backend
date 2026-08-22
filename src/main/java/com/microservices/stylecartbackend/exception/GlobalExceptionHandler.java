package com.microservices.stylecartbackend.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailExists(
            EmailAlreadyExistsException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.CONFLICT,
                "Email Already Exists",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(NameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleNameExists (
        NameAlreadyExistsException ex , HttpServletRequest request) {

        return buildResponse(HttpStatus.CONFLICT,
                    "Name Already Exists" ,
                    ex.getMessage(),
                    request.getRequestURI()
        );
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                "Authentication Failed",
                "Invalid email or password",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleArguement(
            MethodArgumentNotValidException ex , HttpServletRequest request) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "IllegalArguement",
                "invalid input",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        FieldError fieldError = ex.getBindingResult()
                .getFieldErrors()
                .getFirst();

        String message = fieldError != null
                ? fieldError.getDefaultMessage()
                : "Validation failed";

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Validation Failed",
                message,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Validation Failed",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String error,
            String message,
            String path) {

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(status.value())
                .error(error)
                .message(message)
                .path(path)
                .build();

        return ResponseEntity.status(status).body(response);
    }
}