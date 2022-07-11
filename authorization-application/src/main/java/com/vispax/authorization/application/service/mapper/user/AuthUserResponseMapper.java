package com.vispax.authorization.application.service.mapper.user;

import com.vispax.authorization.application.model.User;
import com.vispax.authorization.dto.user.response.AuthUserResponse;
import com.vispax.authorization.dto.user.response.AuthUserResponseList;

import java.util.ArrayList;
import java.util.List;

public class AuthUserResponseMapper {
    public static AuthUserResponseList convert(List<User> lstUser){
        AuthUserResponseList authUserResponseList = new AuthUserResponseList();
        List<AuthUserResponse> lstAuth = new ArrayList<>();
        for(User user :lstUser ){
            lstAuth.add(getAuthUserResponse(user));
        }
        authUserResponseList.setLstUserRole(lstAuth);;
        return authUserResponseList;
    }

    private static AuthUserResponse getAuthUserResponse(User user) {
        AuthUserResponse response = AuthUserResponse.builder().username
                (user.getUsername()).id(user.getId()).roleList(RoleMapper.INSTANCE.convert(user.getRoles())).build();
        return response;
    }



}
