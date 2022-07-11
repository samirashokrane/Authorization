package com.vispax.authorization.application.service.helper;


import com.raiconic.infra.common.crypto.PasswordCrypto;
import lombok.SneakyThrows;
import org.hibernate.service.spi.ServiceException;

import java.io.Serializable;


public class CryptHelper implements Serializable {


    @SneakyThrows
    public static String cryptDecryptActivationCode(String code) throws ServiceException {
        try {

            if (code != null && !code.equalsIgnoreCase("")) {
                if (code.length() > 7) {
                    return PasswordCrypto.decrypt(code);
                } else {
                    return PasswordCrypto.encrypt(code);
                }
            }
        } catch (ServiceException ex) {
            ex.printStackTrace();
        }
        return null;

    }

}