package com.learning.yasminishop.common.exception;


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
    USER_NOT_FOUND(1006, "User not found", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND(1007, "Category not found", HttpStatus.NOT_FOUND),
    SLUG_ALREADY_EXISTS(1008, "Slug already exists", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND(1009, "Product not found", HttpStatus.NOT_FOUND),
    SKU_ALREADY_EXISTS(1010, "SKU already exists", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED(1011, "File upload failed", HttpStatus.INTERNAL_SERVER_ERROR),
    RESOURCE_NOT_FOUND(1012, "Resource not found", HttpStatus.NOT_FOUND),

    // Constraint violation
    INVALID_PASSWORD(2001, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(2002, "Invalid email", HttpStatus.BAD_REQUEST),
    FIELD_NOT_EMPTY(2003, "\"{field}\" must not be empty", HttpStatus.BAD_REQUEST),
    FIELD_NOT_NULL(2004, "\"{field}\" must not be null", HttpStatus.BAD_REQUEST),
    INVALID_DOB(2005, "Your age must be at least {minAge}", HttpStatus.BAD_REQUEST),
    PAGE_MUST_BE_POSITIVE(2006, "Page must be positive", HttpStatus.BAD_REQUEST),
    ITEMS_PER_PAGE_MUST_BE_POSITIVE(2007, "Items per page must be positive", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE(2008, "File size must be less than 5MB", HttpStatus.BAD_REQUEST),
    ;


    private final int internalCode;
    private final String message;
    private final HttpStatusCode statusCode;
}
