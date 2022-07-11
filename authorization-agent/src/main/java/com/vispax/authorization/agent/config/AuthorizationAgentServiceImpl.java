package com.vispax.authorization.agent.config;

import com.raiconic.infra.exception.adapter.AdapterException;
import com.vispax.authorization.dto.login.request.LoginRequest;
import com.vispax.authorization.dto.login.response.UserLoginResponse;
import com.vispax.authorization.dto.logout.request.LogoutRequest;
import com.vispax.authorization.dto.logout.response.LogoutResponse;
import com.vispax.authorization.dto.user.request.AuthChangePassRequest;
import com.vispax.authorization.dto.user.request.AuthInitialRegisterRequest;
import com.vispax.authorization.dto.user.request.AuthUpdateUserInfoRequest;
import com.vispax.authorization.dto.user.response.AuthUserResponse;
import com.vispax.authorization.dto.user.response.AuthUserResponseList;
import com.vispax.authorization.dto.user.response.CheckTokenAuth;
import com.vispax.authorization.dto.user.response.RoleList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class AuthorizationAgentServiceImpl implements AuthorizationAgentService {

    private final RestTemplate restTemplate;

    private String OAUTH_URL="http://localhost:1039";

    @Autowired
    public AuthorizationAgentServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<UserLoginResponse> getAccessToken(final LoginRequest loginReq) {
        HashMap<String, String> paramData = new HashMap<String, String>();
        String data = "";
        paramData.put("username", loginReq.getUsername());
        paramData.put("password", loginReq.getPassword());
        paramData.put("grant_type", loginReq.getGrant_type());
        paramData.put("client-id", loginReq.getClient_id());
        paramData.put("client-secret", loginReq.getClient_secret());
        try {
            data = getDataString(paramData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpHeaders headers = getHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        HttpEntity<String> request = new HttpEntity<>(data, headers);
        ResponseEntity<UserLoginResponse> responseEntity = restTemplate.exchange(OAUTH_URL+"/oauth/token", HttpMethod.POST, request, UserLoginResponse.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw AdapterException.interCommunicationException("ProfileServer", "AuthorizationServer", "username=" + loginReq.getUsername() + "*" +
                    "password=" + loginReq.getPassword() + "*", HttpStatus.SERVICE_UNAVAILABLE);
        }
        return responseEntity;
    }

    public ResponseEntity<CheckTokenAuth> checkToken(String token) {
        HttpHeaders headers = getHeaders();
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        String tokenValue = token.substring("Bearer".length() + 1);
        ResponseEntity<CheckTokenAuth> responseEntity = restTemplate.exchange(OAUTH_URL+"/oauth/check_token?token=" + tokenValue, HttpMethod.GET, request , CheckTokenAuth.class);
        return responseEntity;
    }

    public ResponseEntity<LogoutResponse> logout(LogoutRequest logoutRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", logoutRequest.getAuthorization());
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<LogoutResponse> responseEntity = restTemplate.exchange(OAUTH_URL+"/oauth/logout", HttpMethod.GET, request , LogoutResponse.class);
        return responseEntity;
    }

    @Override
    public ResponseEntity<AuthUserResponse> initialRegister(final String username, final String activationCode) {
        AuthInitialRegisterRequest initUserRegisterRequest = AuthInitialRegisterRequest.builder()
                .password(activationCode)
                .username(username)
                .build();
        HttpEntity<AuthInitialRegisterRequest> request = new HttpEntity<>(initUserRegisterRequest, getHeaders());
        ResponseEntity<AuthUserResponse> responseEntity = restTemplate.exchange(OAUTH_URL+"/boot/user/register", HttpMethod.POST, request, AuthUserResponse.class);
        return responseEntity;
    }

    @Override
    public ResponseEntity<AuthUserResponse> updateUser(final String username, final String email, final String password) {
        AuthUpdateUserInfoRequest updateUserRequest = AuthUpdateUserInfoRequest.builder()
                .email(email)
                .password(password)
                .build();
        HttpEntity<AuthUpdateUserInfoRequest> request = new HttpEntity<>(updateUserRequest, getHeaders());
        ResponseEntity<AuthUserResponse> responseEntity = restTemplate.exchange(OAUTH_URL+"/boot/user/username/" + username, HttpMethod.PUT, request, AuthUserResponse.class);
        return responseEntity;
    }

    @Override
    public ResponseEntity<AuthUserResponse> changePassword(final String username, final String currentPass, final String password) {
        ResponseEntity<UserLoginResponse> response = getAccessToken(new LoginRequest(username, currentPass));
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw AdapterException.interCommunicationException("ProfileServer", "AuthorizationServer", "username=" + username + "*" +
                    "password=" + password + "*", HttpStatus.SERVICE_UNAVAILABLE);
        }

        AuthChangePassRequest changePassRequest = AuthChangePassRequest.builder()
                .username(username)
                .password(password)
                .build();
        HttpEntity<AuthChangePassRequest> request = new HttpEntity<>(changePassRequest, getHeaders());
        ResponseEntity<AuthUserResponse> responseEntity = restTemplate.exchange(OAUTH_URL+"/boot/change-password/" + username, HttpMethod.PUT, request, AuthUserResponse.class);
        return responseEntity;
    }

    @Override
    public ResponseEntity<AuthUserResponse> forgetPassword(final String username, final String password) {
        AuthChangePassRequest changePassRequest = AuthChangePassRequest.builder()
                .username(username)
                .password(password)
                .build();
        HttpEntity<AuthChangePassRequest> request = new HttpEntity<>(changePassRequest, getHeaders());
        ResponseEntity<AuthUserResponse> responseEntity = restTemplate.exchange(OAUTH_URL+"/boot/forget-password/" + username, HttpMethod.PUT, request, AuthUserResponse.class);
        return responseEntity;
    }

    @Override
    public ResponseEntity<AuthUserResponse> assignRoleUser(final String username, final String password) {
        HttpEntity<String> request = new HttpEntity<>(null, getHeaders());
        ResponseEntity<AuthUserResponse> responseEntity = restTemplate.exchange(OAUTH_URL+"/boot/role/assign/user/username/" + username, HttpMethod.POST, request, AuthUserResponse.class);
        return responseEntity;
    }

    @Override
    public ResponseEntity<AuthUserResponseList> getUsersRoles(List<String> lstUserName) {
        HttpEntity<String> request = new HttpEntity<>(null, getHeaders());
        String url = OAUTH_URL+"/boot/roles/"+String.join(",", lstUserName);
        ResponseEntity<AuthUserResponseList> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, AuthUserResponseList.class);

        return responseEntity;
    }

    @Override
    public ResponseEntity<RoleList> getAllRoles() {
        HttpEntity<String> request = new HttpEntity<>(null, getHeaders());
        String url = OAUTH_URL+"/boot/roles";
        ResponseEntity<RoleList> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, RoleList.class);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> setOAUTH2Url(String OAUTH2_URL) {
        this.OAUTH_URL = OAUTH2_URL;
        return new ResponseEntity<>(this.OAUTH_URL, HttpStatus.OK);
    }

    private String getDataString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic bW9iaWxlOnBpbg==");
        headers.add("charset", "utf-8");
        return headers;
    }


}
