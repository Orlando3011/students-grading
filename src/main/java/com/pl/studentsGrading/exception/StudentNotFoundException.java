package com.pl.studentsGrading.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StudentNotFoundException extends RuntimeException {
    private static final String MESSAGE = "No student found for id: ";
    public StudentNotFoundException(Long id) {
        super(MESSAGE + id);
    }
}

