package com.hbwj.exception;

import java.time.LocalDateTime;
import java.util.Map;

class ValidationErrorDetails extends ErrorDetails {
    private Map<String, String> validationErrors;

    public ValidationErrorDetails(LocalDateTime timestamp, String message, String details,
                                  String errorCode, Map<String, String> validationErrors) {
        super(timestamp, message, details, errorCode);
        this.validationErrors = validationErrors;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }
}
