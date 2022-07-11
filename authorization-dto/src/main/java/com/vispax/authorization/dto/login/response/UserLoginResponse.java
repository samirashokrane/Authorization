package com.vispax.authorization.dto.login.response;


import com.raiconic.infra.dto.enums.RoleEnum;
import com.raiconic.infra.dto.response.BaseResponseDto;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserLoginResponse extends BaseResponseDto {
    String access_token;
    String token_type;
    String refresh_token;
    Long expires_in;
    String scope;

    Set<RoleEnum> roles;
}
