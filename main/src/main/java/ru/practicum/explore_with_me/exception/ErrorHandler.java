package ru.practicum.explore_with_me.exception;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {
        log.warn("400 {}", e.getMessage());
        return ErrorResponse
                .builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .status(HttpStatus.BAD_REQUEST.name())
                .message(e.getMessage())
                .reason("Input data validation exception")
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConstraintViolation(final ConstraintViolationException e) {
        log.warn(e.getMessage());
        return ErrorResponse
                .builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .status(HttpStatus.BAD_REQUEST.name())
                .message(e.getMessage())
                .reason("Database interaction exception")
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRequestParam(final MissingRequestValueException e) {
        log.warn(e.getMessage());
        return ErrorResponse
                .builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .status(HttpStatus.BAD_REQUEST.name())
                .message(e.getMessage())
                .reason("Input data validation exception")
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        log.warn(e.getMessage());
        return ErrorResponse
                .builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .status(HttpStatus.BAD_REQUEST.name())
                .message(e.getMessage())
                .reason("Entity not found exception")
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(final ValidationException e) {
        log.warn(e.getMessage());
        return ErrorResponse
                .builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .status(HttpStatus.BAD_REQUEST.name())
                .message(e.getMessage())
                .reason("Input data validation exception")
                .build();
    }
}
