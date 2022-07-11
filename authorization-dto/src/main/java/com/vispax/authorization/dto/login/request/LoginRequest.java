package com.vispax.authorization.dto.login.request;

import com.raiconic.infra.dto.BaseDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest extends BaseDto {

    String username;
    String password;
    final String grant_type = "password";
    final String client_id = "mobile";
    final String client_secret = "pin";

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
