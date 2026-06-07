package com.example.bankcards.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ProblemDetail> handleApplicationException(ApplicationException ex, Locale locale) {
        String message = messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale);
        log.error("An error occurred while processing the request {}", message);

        ProblemDetail details = ProblemDetail.forStatus(ex.getStatus());
        details.setTitle("An error occurred while processing the request");
        details.setDetail(message);

        return ResponseEntity.status(ex.getStatus()).body(details);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(
            MethodArgumentNotValidException exception,
            Locale locale
    ) {
        Map<String, List<String>> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        LinkedHashMap::new,
                        Collectors.mapping(
                                error -> messageSource.getMessage(error, locale),
                                Collectors.toList()
                        )
                ));

        ProblemDetail details = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        details.setTitle("Validation Failed");
        details.setDetail("Fields are not valid");
        details.setProperty("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(details);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception ex) {
        log.error("An error occurred while processing the request", ex);

        return ResponseEntity.internalServerError().build();
    }
}
