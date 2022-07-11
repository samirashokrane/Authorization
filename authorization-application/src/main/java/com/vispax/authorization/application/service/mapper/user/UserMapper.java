package com.vispax.authorization.application.service.mapper.user;

import com.raiconic.infra.dto.enums.RoleEnum;
import com.vispax.authorization.application.model.User;
import com.vispax.authorization.application.model.UserRole;
import com.vispax.authorization.dto.user.request.AuthChangePassRequest;
import com.vispax.authorization.dto.user.request.AuthInitialRegisterRequest;
import com.vispax.authorization.dto.user.request.AuthUpdateUserInfoRequest;
import com.vispax.authorization.dto.user.response.AuthUserResponse;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class UserMapper {

    public static final UserMapper INSTANCE = new UserMapper();

    public User initialize(final AuthInitialRegisterRequest request, final String password){
        return User.builder()
                .activationCode(request.getPassword())
                .password(password)
                .username(request.getUsername())
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .enabled(true)
                .email(request.getUsername()+"@vispax.com")
                .build();
    }

    public AuthUserResponse convert(final User user, final List<UserRole> userRoleList){
        return AuthUserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roleList(
                        userRoleList.stream().map(userRole -> RoleEnum.fromString(userRole.getRole().getName().getValue())).collect(Collectors.toList())
                )
                .build();
    }

    public User convert(User user, final AuthUpdateUserInfoRequest request){
        user.setEmail(request.getEmail()!=null ? request.getEmail() : user.getEmail());
        user.setPassword(request.getPassword()!=null ? request.getPassword() : user.getPassword());
        return user;
    }
    public User convert(User user, final AuthChangePassRequest request){

        user.setPassword(request.getPassword()!=null ? request.getPassword() : user.getPassword());
        return user;
    }
}
