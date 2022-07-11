package com.vispax.authorization.application.service;


import com.raiconic.infra.dto.enums.RoleEnum;
import com.raiconic.infra.exception.service.facade.FacadeServiceException;
import com.raiconic.infra.exception.service.logic.ReadableServiceException;
import com.raiconic.infra.exception.service.logic.WritableServiceException;
import com.raiconic.infra.service.facade.BaseFacadeService;
import com.vispax.authorization.application.model.Role;
import com.vispax.authorization.application.model.User;
import com.vispax.authorization.application.model.UserRole;
import com.vispax.authorization.application.repository.RoleRepository;
import com.vispax.authorization.application.repository.UserDetailRepository;
import com.vispax.authorization.application.repository.UserRoleRepository;
import com.vispax.authorization.application.service.helper.CryptHelper;
import com.vispax.authorization.application.service.mapper.user.AuthUserResponseMapper;
import com.vispax.authorization.application.service.mapper.user.UserMapper;
import com.vispax.authorization.dto.VerifyUserReq;
import com.vispax.authorization.dto.user.request.AuthChangePassRequest;
import com.vispax.authorization.dto.user.request.AuthInitialRegisterRequest;
import com.vispax.authorization.dto.user.request.AuthUpdateUserInfoRequest;
import com.vispax.authorization.dto.user.response.AuthUserResponse;
import com.vispax.authorization.dto.user.response.AuthUserResponseList;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl extends BaseFacadeService implements UserService {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);


    private final UserDetailRepository userDetailRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final TokenStore jdbcTokenStore;


    @Override
    public User findByUsername(String userName) {
        // TODO Auto-generated method stub
        Optional<User> user = userDetailRepository.findByUsername(userName);
        if (user.isPresent()) {
            return user.get();
        }
        return null;

    }

    @Override
    public User findById(Integer id) {
        Optional<User> user = userDetailRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            return null;
        }

    }

    @Override
    public boolean logout(final HttpServletRequest request) {
        logger.info("logout ....");
        String authHeader = request.getHeader("Authorization");
        logger.info("revoke token ....{}", request);
        try {
            if (request != null) {
                String tokenValue = authHeader.substring("Bearer".length() + 1);
                OAuth2AccessToken accessToken = jdbcTokenStore.readAccessToken(tokenValue);
                jdbcTokenStore.removeAccessToken(accessToken);
                return true;
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }



    @Override
    public AuthUserResponse initialRegister(final AuthInitialRegisterRequest request) {

        Optional<User> optionalUser = userDetailRepository.findByUsername(request.getUsername());
        if (optionalUser.isPresent()) {
            throw WritableServiceException.alreadyExistException(request.getUsername(), "username", HttpStatus.BAD_REQUEST);
        }
        User user = UserMapper.INSTANCE.initialize(request, passwordEncoder.encode(request.getPassword()));

        user = userDetailRepository.save(user);
        List<UserRole> userRoleList = userRoleRepository.findByUserId(user.getId());
        if (userRoleList.stream().filter(userRole -> userRole.getRole().getName().getValue().equals(RoleEnum.USER)).findFirst().isPresent()) {
            throw WritableServiceException.alreadyExistException(request.getUsername(), "username", HttpStatus.BAD_REQUEST);
        }
        Role role = roleRepository.findFirstByName(RoleEnum.USER).orElseThrow(() -> FacadeServiceException.internalServerException("role", RoleEnum.USER.name(), HttpStatus.INTERNAL_SERVER_ERROR));
        UserRole userRole = UserRole.builder().role(role).roleId(role.getId()).user(user).userId(user.getId()).build();
        userRole = userRoleRepository.save(userRole);
        userRoleList = userRoleRepository.findByUserId(user.getId());
        return UserMapper.INSTANCE.convert(user, userRoleList);

//        Random randomGenerator = new Random();
//        Integer randomInt = 100000 + randomGenerator.nextInt(8900);
//        // Integer randomInt = 123456;
//        String actCODE = createActivationCode(randomInt.toString());
//        String password = createPassword(randomInt.toString());
//
//        if (user != null && user.isPresent()) {
//            user.get().setPassword(password);
//            user.get().setActivationCode(actCODE);
//            userDetailRepository.save(user.get());
//            System.out.println(randomInt);
//            System.out.println(randomInt);
//            System.out.println(randomInt);
//            return randomInt.toString();
//        }
//        User userNew = new User();
//        userNew.setUsername(request.getUserName());
//        userNew.setAccountNonExpired(true);
//        userNew.setAccountNonLocked(true);
//        userNew.setCredentialsNonExpired(true);
//        userNew.setEnabled(true);
//        userNew.setPassword(password);
//        userNew.setActivationCode(actCODE);
//        userNew.setEmail(request.getUserName() + "@baroline.com");
//        userNew = userDetailRepository.save(userNew);
//        logger.info("Add User Role ....");
//        UserRole userRole = new UserRole();
//        userRole.setRoleId(RoleEnum.USER.getId());
//        userRole.setUserId(userNew.getId());
//        userRole = userRoleRepository.save(userRole);
//        logger.info("Save Successfuly User Into ouath db");
//        return randomInt.toString();

    }


    @Override
    public AuthUserResponse updateUserInfo(final String username, final AuthUpdateUserInfoRequest request) {
        Optional<User> optionalUser = userDetailRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw ReadableServiceException.notFoundException(username, "username", HttpStatus.BAD_REQUEST);
        }
        User user = UserMapper.INSTANCE.convert(optionalUser.get(), request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = userDetailRepository.save(user);
        List<UserRole> userRoleList = userRoleRepository.findByUserId(user.getId());
        return UserMapper.INSTANCE.convert(user, userRoleList);
    }

    @Override
    public AuthUserResponse changePassword(final String username, final AuthChangePassRequest request) {
        Optional<User> optionalUser = userDetailRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw ReadableServiceException.notFoundException(username, "username", HttpStatus.BAD_REQUEST);
        }
        User user = UserMapper.INSTANCE.convert(optionalUser.get(), request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = userDetailRepository.save(user);
        List<UserRole> userRoleList = userRoleRepository.findByUserId(user.getId());
        return UserMapper.INSTANCE.convert(user, userRoleList);
    }

    @Override
    public AuthUserResponse forgetPassword(final String username, final AuthChangePassRequest request) {
        Optional<User> optionalUser = userDetailRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw ReadableServiceException.notFoundException(username, "username", HttpStatus.BAD_REQUEST);
        }
        User user = UserMapper.INSTANCE.convert(optionalUser.get(), request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = userDetailRepository.save(user);
        List<UserRole> userRoleList = userRoleRepository.findByUserId(user.getId());
        return UserMapper.INSTANCE.convert(user, userRoleList);
    }

    @Override
    public AuthUserResponse assignRoleUser(final String username) {
        Optional<User> optionalUser = userDetailRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw ReadableServiceException.notFoundException(username, "username", HttpStatus.BAD_REQUEST);
        }
        List<UserRole> userRoleList = userRoleRepository.findByUserId(optionalUser.get().getId());
        if (userRoleList.stream().anyMatch(userRole -> userRole.getRole().getName().getValue().equals(RoleEnum.USER))) {
            throw WritableServiceException.alreadyExistException(username, "username", HttpStatus.BAD_REQUEST);
        }
        Role role = roleRepository.findFirstByName(RoleEnum.USER).orElseThrow(() -> ReadableServiceException.notFoundException(RoleEnum.USER.name(), "role", HttpStatus.NOT_FOUND));
        UserRole userRole = UserRole.builder()
                .role(role)
                .roleId(role.getId())
                .user(optionalUser.get())
                .userId(optionalUser.get().getId())
                .build();
        userRole = userRoleRepository.save(userRole);
        userRoleList = userRoleRepository.findByUserId(optionalUser.get().getId());
        return UserMapper.INSTANCE.convert(optionalUser.get(), userRoleList);
    }

    @Override
    public String editUser(AuthInitialRegisterRequest request) {
        Optional<User> user = userDetailRepository.findByUsername(request.getUsername());
        if (user != null && user.isPresent()) {
            String regex2 = "[0-9]+";
            if (request != null && StringUtils.hasLength(request.getPassword())) {
                boolean isValid = request.getPassword().trim().matches(regex2);
                if (!isValid) {
                    return "Fail Change Password User profile";
                }
                String actCODE = createActivationCode(request.getPassword());
                String password = createPassword(request.getPassword());
                user.get().setPassword(password);
                user.get().setActivationCode(actCODE);
                userDetailRepository.save(user.get());
            }
        }
        return user.get().getUsername();
    }

    private boolean checkUserRole(Integer roleId, List<UserRole> lstUserRole) {
        for (UserRole userRole : lstUserRole) {
            if (userRole.getRoleId().equals(roleId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String deleteUser(AuthInitialRegisterRequest request) {
        Optional<User> user = userDetailRepository.findByUsername(request.getUsername());
        if (user != null && user.isPresent()) {
            userRoleRepository.deleteUserRole(user.get().getId(), 5);
            userDetailRepository.deleteUser(user.get().getUsername());
        }
        return "true";
    }

    @Override
    public AuthUserResponseList getRoles(List<String> usernames) {
        List<User>  lstUser =  userDetailRepository.findAllUserByNames(usernames);
        return AuthUserResponseMapper.convert(lstUser);
    }

    public String createActivationCode(String activationCode) throws ServiceException {
        String actCODE = "";
        if (StringUtils.hasLength(activationCode)) {
            actCODE = CryptHelper.cryptDecryptActivationCode(activationCode);
        }
        return actCODE;
    }

    public String createPassword(String password) throws ServiceException {
        String hashPassword = "";
        if (StringUtils.hasLength(password)) {
            hashPassword = passwordEncoder.encode(password);
        } else {
            hashPassword = passwordEncoder.encode("123456");
        }
        return hashPassword;
    }

    @Override
    public String checkActivetionCode(VerifyUserReq verifyUserReq) throws ServiceException {
        String activeCodeUser = "";
        Optional<User> user = userDetailRepository.findByUsername(verifyUserReq.getUserName());
        if (user != null && user.isPresent()) {
            activeCodeUser = CryptHelper.cryptDecryptActivationCode(user.get().getActivationCode());
            if (verifyUserReq.getActivationCode().equalsIgnoreCase(activeCodeUser)) {
                return "true";
            }
        }
        return "false";
    }

    @Override
    public String getActiveCode(String userName) throws ServiceException {
        String activeCodeUser = "";
        Optional<User> user = userDetailRepository.findByUsername(userName);
        if (user != null && user.isPresent()) {
            activeCodeUser = CryptHelper.cryptDecryptActivationCode(user.get().getActivationCode());
        } else {
            activeCodeUser = "-1";
        }
        return activeCodeUser;
    }
}
