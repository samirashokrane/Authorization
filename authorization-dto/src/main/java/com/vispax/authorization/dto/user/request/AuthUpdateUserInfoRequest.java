package com.vispax.authorization.dto.user.request;

import com.raiconic.infra.dto.request.BaseRequestDto;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthUpdateUserInfoRequest extends BaseRequestDto {

    String password;
    String email;

}
