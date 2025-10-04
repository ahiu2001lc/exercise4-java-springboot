package com.example.exercise4.exception;

public class ExceptionMessages {
    public static final String VALIDATION_FAILED = "Validation Failed";
    public static final String BAD_REQUEST = "Duplicated";
    public static final String NOT_FOUND = "Not Found";

    private ExceptionMessages() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
