package com.vispax.authorization.application.service;

import com.vispax.authorization.application.model.User;
import com.vispax.authorization.dto.VerifyUserReq;
import com.vispax.authorization.dto.user.request.AuthChangePassRequest;
import com.vispax.authorization.dto.user.request.AuthInitialRegisterRequest;
import com.vispax.authorization.dto.user.request.AuthUpdateUserInfoRequest;
import com.vispax.authorization.dto.user.response.AuthUserResponse;
import com.vispax.authorization.dto.user.response.AuthUserResponseList;
import org.hibernate.service.spi.ServiceException;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

public interface UserService extends Serializable{
	User findByUsername(String userName) ;

	User findById(Integer id) ;

	boolean logout(HttpServletRequest request);

	AuthUserResponse initialRegister(final AuthInitialRegisterRequest request) ;

	AuthUserResponse updateUserInfo(final String username, final AuthUpdateUserInfoRequest request) ;

	AuthUserResponse changePassword(final String username, final AuthChangePassRequest request) ;

	AuthUserResponse forgetPassword(final String username, final AuthChangePassRequest request) ;

	AuthUserResponse assignRoleUser(final String username) ;

	public String checkActivetionCode(VerifyUserReq verifyUserReq) throws ServiceException;

	public String getActiveCode(String userName) throws ServiceException;

    String editUser(AuthInitialRegisterRequest request);

	String deleteUser(AuthInitialRegisterRequest request);

	AuthUserResponseList  getRoles(List<String> usernames);

}
