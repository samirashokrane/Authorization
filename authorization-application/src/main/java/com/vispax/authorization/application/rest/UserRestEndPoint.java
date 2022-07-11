package com.vispax.authorization.application.rest;

import com.vispax.authorization.application.service.RoleService;
import com.vispax.authorization.application.service.UserService;
import com.vispax.authorization.application.service.UserServiceImpl;
import com.vispax.authorization.dto.VerifyUserReq;
import com.vispax.authorization.dto.user.request.AuthChangePassRequest;
import com.vispax.authorization.dto.user.request.AuthInitialRegisterRequest;
import com.vispax.authorization.dto.user.request.AuthUpdateUserInfoRequest;
import com.vispax.authorization.dto.user.response.AuthUserResponse;
import com.vispax.authorization.dto.user.response.AuthUserResponseList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/boot")
@CrossOrigin(origins = "*")
public class UserRestEndPoint implements Serializable {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @ResponseBody
    @PostMapping("/user/register")
    public ResponseEntity<AuthUserResponse> initialRegister(@RequestBody AuthInitialRegisterRequest request) throws Exception {
        return new ResponseEntity<>(userService.initialRegister(request), HttpStatus.OK);
    }

    @ResponseBody
    @PutMapping("/user/username/{username}")
    public ResponseEntity<AuthUserResponse> updateUserInfo(@PathVariable("username") final String username, @RequestBody AuthUpdateUserInfoRequest request) throws Exception {
        return new ResponseEntity<>(userService.updateUserInfo(username, request), HttpStatus.OK);
    }

    @ResponseBody
    @PutMapping("/change-password/{username}")
    public ResponseEntity<AuthUserResponse> changePassword(@PathVariable("username") final String username, @RequestBody AuthChangePassRequest request) throws Exception {
        return new ResponseEntity<>(userService.changePassword(username, request), HttpStatus.OK);
    }


    @ResponseBody
    @PutMapping("/forget-password/{username}")
    public ResponseEntity<AuthUserResponse> forgetPassword(@PathVariable("username") final String username, @RequestBody AuthChangePassRequest request) throws Exception {
        return new ResponseEntity<>(userService.forgetPassword(username, request), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/role/assign/user/username/{username}")
    public ResponseEntity<AuthUserResponse> assignRoleUser(@PathVariable("username") final String username) throws Exception {
        return new ResponseEntity<>(userService.assignRoleUser(username), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/edituser")
    public String editUser(HttpServletRequest request, @RequestBody AuthInitialRegisterRequest initRequest) throws Exception {
        return userService.editUser(initRequest);
    }

    @ResponseBody
    @DeleteMapping("/deleteuser")
    public String deleteUser(HttpServletRequest request, @RequestBody AuthInitialRegisterRequest initRequest) throws Exception {
        return userService.deleteUser(initRequest);
    }

    @ResponseBody
    @PostMapping("/verifyuser")
    public String checkActivationCode(HttpServletRequest request, @RequestBody VerifyUserReq verifyUserReq) throws Exception {
        return userService.checkActivetionCode(verifyUserReq);
    }

    @ResponseBody
    @PostMapping("/activationcode")
    public String getActivationCode(HttpServletRequest request, @RequestBody String mobile) throws Exception {
        return userService.getActiveCode(mobile);
    }

    @ResponseBody
    @GetMapping("/roles/{usernames}")
    public ResponseEntity<AuthUserResponseList> userRoles(HttpServletRequest request, @PathVariable("usernames") final List<String> lstUserName) throws Exception {
        return new ResponseEntity<AuthUserResponseList>(userService.getRoles(lstUserName), HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/getusernames")
    public ResponseEntity<List<String>> getUserName() throws Exception {
        List<String> lstUser = new ArrayList<>();
        lstUser.add("admin@vispax.com");
        lstUser.add("09914235174");
        return new ResponseEntity<List<String>>(lstUser, HttpStatus.OK);
    }
}

