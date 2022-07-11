//package com.vispax.authorization.application.model;
//
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//
//@Converter
//@Component
//@AllArgsConstructor
//public class PasswordEncoderConverter implements AttributeConverter<String, String> {
//
//    private final PasswordEncoder passwordEncoder;
//
//
//    @Override
//    public String convertToDatabaseColumn(String password) {
//            return passwordEncoder.encode(password);
//    }
//
//    @Override
//    public String convertToEntityAttribute(String password) {
//        return password;
//    }
//}
