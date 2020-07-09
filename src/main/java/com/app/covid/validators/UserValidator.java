package com.app.covid.validators;

import com.app.covid.models.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == User.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (StringUtils.isBlank(user.getEmail())) {
            errors.rejectValue("email", HttpStatus.BAD_REQUEST.getReasonPhrase(), "Email can not be empty.");
        }
        if (StringUtils.isBlank(user.getFirstName())) {
            errors.rejectValue("firstName", HttpStatus.BAD_REQUEST.getReasonPhrase(), "First name can not be empty.");
        }
        if (StringUtils.isBlank(user.getLastName())) {
            errors.rejectValue("lastName", HttpStatus.BAD_REQUEST.getReasonPhrase(), "Last name can not be empty.");
        }
        if (StringUtils.isBlank(user.getPassword()) || user.getPassword().length() < 8) {
            errors.rejectValue("password", HttpStatus.BAD_REQUEST.getReasonPhrase(), "Password should be at least of 8 characters.");
        }
    }
}
