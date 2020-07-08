package com.app.covid.errorhandler.components;

import com.app.covid.errorhandler.exceptions.CovidException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CovidErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions errorAttributeOptions) {
        Map<String, Object> map = super.getErrorAttributes(request, errorAttributeOptions);
        map.clear();
        Integer status;
        String message;
        List<String> errors;

        Throwable throwable = getError(request);
        if (throwable instanceof CovidException) {
            CovidException ex = (CovidException) getError(request);
            status = ex.getStatus().value();
            message = ex.getMessage();
            errors = ex.getErrors();
        } else if (throwable instanceof WebExchangeBindException) {
            WebExchangeBindException ex = (WebExchangeBindException) getError(request);
            status = ex.getStatus().value();
            List<ObjectError> objectErrors = ex.getAllErrors();
            message = ex.getStatus().getReasonPhrase();
            errors = objectErrors.parallelStream().map(ObjectError::getDefaultMessage).collect(Collectors.toList());
        } else if (throwable instanceof ResponseStatusException) {
            ResponseStatusException ex = (ResponseStatusException) getError(request);
            status = ex.getStatus().value();
            message = ex.getMessage();
            errors = Collections.singletonList(ex.getStatus().getReasonPhrase());
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR.value();
            message = "Something went wrong, please try again";
            errors = Collections.singletonList(throwable.getMessage());
        }

        map.put("status", status);
        map.put("message", message);
        if (CollectionUtils.isNotEmpty(errors)) {
            map.put("errors", errors);
        }
        return map;
    }
}
