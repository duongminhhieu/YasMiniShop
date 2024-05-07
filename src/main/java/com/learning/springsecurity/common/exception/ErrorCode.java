package com.learning.springsecurity.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_TOKEN(1001, "Invalid token", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS(1002, "Email already exists", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND(1003, "Email not found", HttpStatus.NOT_FOUND),
    UNAUTHORIZED(1004, "You do not have permission", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(1005, "Unauthenticated", HttpStatus.UNAUTHORIZED),

    // Constraint violation
    INVALID_PASSWORD(2001, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(2002, "Invalid email", HttpStatus.BAD_REQUEST),
    FIELD_NOT_EMPTY(2003, "\"{field}\" must not be empty", HttpStatus.BAD_REQUEST),
    FIELD_NOT_NULL(2004, "\"{field}\" must not be null", HttpStatus.BAD_REQUEST),

    ;


    private final int internalCode;
    private final String message;
    private final HttpStatusCode statusCode;
}
