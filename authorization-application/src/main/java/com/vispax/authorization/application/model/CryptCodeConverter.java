package com.vispax.authorization.application.model;

import com.raiconic.infra.common.crypto.CryptHelper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CryptCodeConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(final String rawCode) {
        return CryptHelper.encryptCode(rawCode);
    }

    @Override
    public String convertToEntityAttribute(final String encryptedCode) {
        return CryptHelper.decryptCode(encryptedCode);
    }
}
