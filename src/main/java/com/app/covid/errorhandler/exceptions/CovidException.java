package com.app.covid.errorhandler.exceptions;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


public class CovidException extends ResponseStatusException {

    private List<String> errors;

    public CovidException(HttpStatus status, String message, Throwable e) {
        super(status, message, e);
    }

    public CovidException(HttpStatus status, String message, List<String> errors) {
        super(status, message);
        this.errors = errors;
    }

    public CovidException(HttpStatus status, String message, List<String> errors, Throwable e) {
        super(status, message, e);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public String getMessage() {
        String msg = super.getReason() != null ? super.getReason() : super.getStatus().getReasonPhrase();
        return NestedExceptionUtils.buildMessage(msg, getCause());
    }
}
