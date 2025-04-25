package com.eli.ads.common.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handle(MethodArgumentNotValidException exception, HttpServletRequest request){

        String errorMessage = exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" | "));

        return ExceptionResponse
                .builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .errorType(ErrorType.VALIDATION)
                .timestamp(Instant.now())
                .message(errorMessage)
                .path(request.getRequestURI())
                .build();

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handle(ResourceNotFoundException exp, HttpServletRequest request){

        return ExceptionResponse
                .builder()
                .code(HttpStatus.NOT_FOUND.value())
                .errorType(ErrorType.VALIDATION)
                .timestamp(Instant.now())
                .message(exp.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(ActionNotAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse handle(ActionNotAuthorizedException exp, HttpServletRequest request){

        return ExceptionResponse
                .builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .errorType(ErrorType.BUSINESS)
                .timestamp(Instant.now())
                .message(exp.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handle(EntityNotFoundException exp, HttpServletRequest request){

        return ExceptionResponse
                .builder()
                .code(HttpStatus.NOT_FOUND.value())
                .errorType(ErrorType.VALIDATION)
                .timestamp(Instant.now())
                .message(exp.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handle(IllegalArgumentException exp, HttpServletRequest request){

        return ExceptionResponse
                .builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorType(ErrorType.VALIDATION)
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .message(exp.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handle(Exception exp, HttpServletRequest request){

        return ExceptionResponse
                .builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorType(ErrorType.VALIDATION)
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .message("Something went wrong but it's on us. Refresh and retry or contact us")
                .message(exp.getMessage())
                .build();
    }
}
