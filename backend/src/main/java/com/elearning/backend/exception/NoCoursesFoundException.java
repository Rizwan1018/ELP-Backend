package com.elearning.backend.exception;

public class NoCoursesFoundException extends RuntimeException {
    public NoCoursesFoundException(String message) {
        super(message);
    }
}
