package com.vispax.authorization.agent.config;


import com.vispax.authorization.dto.login.request.LoginRequest;
import com.vispax.authorization.dto.login.response.UserLoginResponse;
import com.vispax.authorization.dto.logout.request.LogoutRequest;
import com.vispax.authorization.dto.logout.response.LogoutResponse;
import com.vispax.authorization.dto.user.response.AuthUserResponse;
import com.vispax.authorization.dto.user.response.AuthUserResponseList;
import com.vispax.authorization.dto.user.response.CheckTokenAuth;
import com.vispax.authorization.dto.user.response.RoleList;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface AuthorizationAgentService {


     ResponseEntity<UserLoginResponse> getAccessToken(final LoginRequest request);

     ResponseEntity<CheckTokenAuth> checkToken(final String username);

     ResponseEntity<LogoutResponse>  logout(LogoutRequest request);

     ResponseEntity<AuthUserResponse> initialRegister(final String username, final String activationCode);

     ResponseEntity<AuthUserResponse> updateUser(final String username, final String email, final String password);

     ResponseEntity<AuthUserResponse> changePassword(final String username, final String currentPass, final String newPass);

     ResponseEntity<AuthUserResponse> forgetPassword(final String username, final String newPass);

     ResponseEntity<AuthUserResponse> assignRoleUser(final String username, final String password);

     ResponseEntity<AuthUserResponseList> getUsersRoles(final List<String> lstUserName);

     ResponseEntity<RoleList>  getAllRoles();


     ResponseEntity<String>  setOAUTH2Url(String OAUTH2_URL);

}
