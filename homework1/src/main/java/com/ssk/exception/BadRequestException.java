package com.ssk.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends GlobalException {
	private static final long serialVersionUID = -1L;


    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST,"Bad request");
    }

    public BadRequestException(Throwable cause) {
        super(HttpStatus.BAD_REQUEST,"Bad request", cause);
    }
}
