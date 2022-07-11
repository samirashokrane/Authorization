package com.vispax.authorization.dto.user.response;

import com.raiconic.infra.dto.response.BaseResponseDto;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;


@Data
@ToString
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class CheckTokenAuth extends BaseResponseDto {

    private String[] aud;

    private String user_name;

    private String[] scope;

    private String active;

    private String exp;

    private String[] authorities;

    private String client_id;

}
