/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

import static com.somecode.utils.Utils.getMessage;

class ValidationError {
    private final static String VALIDATION_FAILED = "validationError.validationFailed";
    private final String errorMessage;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> errors = new ArrayList<>();

    private ValidationError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    static ValidationError fromBindingErrors(Errors errors) {
        ValidationError error
                = new ValidationError(getMessage(VALIDATION_FAILED, new Object[]{errors.getErrorCount()}));
        for (ObjectError objectError : errors.getAllErrors()) {
            error.addValidationError(objectError.getDefaultMessage());
        }
        return error;
    }

    private void addValidationError(String error) {
        errors.add(error);
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
