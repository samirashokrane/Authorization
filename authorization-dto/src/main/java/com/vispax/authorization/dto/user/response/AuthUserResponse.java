package com.vispax.authorization.dto.user.response;


import com.raiconic.infra.dto.enums.RoleEnum;
import com.raiconic.infra.dto.response.BaseResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthUserResponse extends BaseResponseDto {

    private static final long serialVersionUID = 1L;

    Long id;
    List<RoleEnum> roleList = new ArrayList<>();
    String username;


}
