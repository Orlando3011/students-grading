package com.pl.studentsGrading.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IdNotNullException extends RuntimeException {
    private final static String MESSAGE = "Id must be null but is: ";
    public IdNotNullException(Long id) {
        super(MESSAGE + id);
    }
}
