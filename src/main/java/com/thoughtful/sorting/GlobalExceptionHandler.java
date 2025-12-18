package com.thoughtful.sorting;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        List<String> messages = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            messages.add(error.getDefaultMessage());
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Failed",
            messages,
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        
        List<String> messages = new ArrayList<>();
        String userFriendlyMessage = "Invalid request body";
        
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) cause;
            String fieldName = getFieldName(ife);
            String targetType = getSimpleTypeName(ife.getTargetType().getSimpleName());
            Object value = ife.getValue();
            
            if (value instanceof String) {
                userFriendlyMessage = String.format(
                    "Invalid value '%s' for field '%s'. Expected a valid %s.",
                    value, fieldName, targetType
                );
            } else {
                userFriendlyMessage = String.format(
                    "Invalid value for field '%s'. Expected a valid %s.",
                    fieldName, targetType
                );
            }
        } else if (ex.getMessage().contains("Required request body is missing")) {
            userFriendlyMessage = "Request body is required";
        } else if (ex.getMessage().contains("JSON parse error")) {
            userFriendlyMessage = "Invalid JSON format. Please check your request structure.";
        }
        
        messages.add(userFriendlyMessage);
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid Request",
            messages,
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        
        List<String> messages = new ArrayList<>();
        messages.add("Invalid value for parameter '" + ex.getName() + "': " + ex.getValue());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid Input",
            messages,
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        List<String> messages = new ArrayList<>();
        messages.add("An unexpected error occurred. Please contact support if the problem persists.");
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            messages,
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    
    private String getFieldName(InvalidFormatException ex) {
        if (!ex.getPath().isEmpty()) {
            JsonMappingException.Reference reference = ex.getPath().get(ex.getPath().size() - 1);
            return reference.getFieldName();
        }
        return "unknown";
    }
    
    private String getSimpleTypeName(String typeName) {
        switch (typeName) {
            case "Double":
                return "number (e.g., 10.5, 150.0)";
            case "Integer":
                return "whole number (e.g., 10, 150)";
            case "String":
                return "text";
            case "Boolean":
                return "true or false";
            default:
                return typeName.toLowerCase();
        }
    }
}

