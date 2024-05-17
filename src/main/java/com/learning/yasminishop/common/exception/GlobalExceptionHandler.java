package com.learning.yasminishop.common.exception;

import com.learning.yasminishop.common.dto.APIResponse;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        log.error("Exception: ", exception);
        var apiResponse = APIResponse.builder()
                .internalCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getInternalCode())
                .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Object> handleAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        var apiResponse = APIResponse.builder()
                .internalCode(errorCode.getInternalCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResourceFoundException() {
        ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;
        var apiResponse = APIResponse.builder()
                .internalCode(errorCode.getInternalCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }


    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Object> handleJwtException() {
        ErrorCode errorCode = ErrorCode.INVALID_TOKEN;
        var apiResponse = APIResponse.builder()
                .internalCode(errorCode.getInternalCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    ResponseEntity<Object> handlingAuthenticationException() {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        var apiResponse = APIResponse.builder()
                .internalCode(errorCode.getInternalCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }


    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<Object> handlingAccessDeniedException() {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        var apiResponse = APIResponse.builder()
                .internalCode(errorCode.getInternalCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException exception) {
        String enumKey = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        ErrorCode errorCode;
        Map<String, Object> attributes = null;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
            var constraintViolation = exception.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);
            attributes = constraintViolation.getConstraintDescriptor().getAttributes();
            log.info("Error attribute: " + attributes);
            log.info("Error message: " + enumKey);
            log.info(attributes.toString());
        } catch (IllegalArgumentException e) {
            log.error("Invalid error code: " + enumKey, e);
            errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        }

        String message = Objects.nonNull(attributes) ? mapAttributes(errorCode.getMessage(), attributes) : errorCode.getMessage();
        var apiResponse = APIResponse.builder()
                .internalCode(errorCode.getInternalCode())
                .message(message)
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String mapAttributes(String message, Map<String, Object> attributes) {
        List<String> keys = extractKeyFromMessage(message);

        // replace attributes by key
        for (String key : keys) {
            String value = String.valueOf(attributes.get(key));
            message = message.replace("{" + key + "}", value);
        }

        return message;
    }

    private List<String> extractKeyFromMessage(String message) {
        Pattern pattern = Pattern.compile("\\{([^{}]*)}");
        Matcher matcher = pattern.matcher(message);

        List<String> keys = new ArrayList<>();

        while (matcher.find()) {
            String key = matcher.group(1);
            keys.add(key);
        }

        return keys;
    }

}
