package com.app.covid.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PasswordConverter implements AttributeConverter<String, String> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return passwordEncoder.encode(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }
}
