package com.skiply.system.common.api.jackson.error.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.skiply.system.common.api.jackson.error.dto.ErrorDTO;
import com.skiply.system.common.domain.exception.DomainEntityNotFoundException;
import com.skiply.system.common.domain.exception.DomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String CLIENT_ERROR = "client_side_error";
    public static final String SERVER_ERROR = "server_side_error";

    @ExceptionHandler(DomainEntityNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    ErrorDTO notFoundErrorHandler(final DomainEntityNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return ErrorDTO.builder()
                .code(CLIENT_ERROR)
                .messages(List.of(exception.getMessage()))
                .build();
    }

    @ExceptionHandler(DomainException.class)
    @ResponseStatus(BAD_REQUEST)
    ErrorDTO badRequestErrorHandler(final DomainException exception) {
        log.error(exception.getMessage(), exception);
        return ErrorDTO.builder()
                .code(CLIENT_ERROR)
                .messages(List.of(exception.getMessage()))
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    ErrorDTO pathParamValidatorConstraintErrorHandler(final ConstraintViolationException exception) {
        final var errorMessages = exception
                .getConstraintViolations()
                .stream()
                .map(violation -> String.format("%s: %s",
                        StreamSupport.stream(violation.getPropertyPath().spliterator(), false)
                                .map(Path.Node::getName)
                                .reduce((s, s2) -> s2)
                                .orElse("unknown"),
                        violation.getMessage()))
                .toList();
        log.error("{}", errorMessages, exception);

        return ErrorDTO.builder()
                .code(CLIENT_ERROR)
                .messages(errorMessages)
                .build();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    ErrorDTO validationExceptionErrorHandler(final ValidationException validationException) {
        final var errorMessage = validationException.getMessage();
        log.error(errorMessage, validationException);

        return ErrorDTO.builder()
                .code(CLIENT_ERROR)
                .messages(List.of(errorMessage))
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    ErrorDTO bodyValidatorConstraintErrorHandler(final MethodArgumentNotValidException exception) {
        final var errorMessages = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> String.format("%s: %s",
                        fieldError.getField(),
                        fieldError.getDefaultMessage()))
                .toList();

        log.error("{}", errorMessages, exception);
        return ErrorDTO.builder()
                .code(CLIENT_ERROR)
                .messages(errorMessages)
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(BAD_REQUEST)
    ErrorDTO jsonMappingExceptionErrorHandler(final HttpMessageNotReadableException exception) {
        final List<String> errorMessages;
        if (exception.getCause() instanceof JsonMappingException error) {
            errorMessages = error.getPath()
                    .stream()
                    .map(reference -> String.format("%s: %s",
                            reference.getFieldName(),
                            error.getOriginalMessage()))
                    .toList();

            log.error("{}", errorMessages, error);
        } else {
            errorMessages = List.of(exception.getCause().getMessage());
            log.error("{}", errorMessages, exception.getCause());
        }

        return ErrorDTO.builder()
                .code(CLIENT_ERROR)
                .messages(errorMessages)
                .build();
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    ErrorDTO internalServerErrorHandler(final RuntimeException exception) {
        log.error(exception.getMessage(), exception);
        return ErrorDTO.builder()
                .code(SERVER_ERROR)
                .messages(List.of(INTERNAL_SERVER_ERROR.getReasonPhrase()))
                .build();
    }
}
