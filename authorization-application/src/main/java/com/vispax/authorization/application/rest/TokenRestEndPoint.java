package com.vispax.authorization.application.rest;

import com.vispax.authorization.application.service.UserService;
import com.vispax.authorization.application.service.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;


@RestController
@RequestMapping(value = "/oauth")
@CrossOrigin(origins = "*")
public class TokenRestEndPoint implements Serializable {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean logout(HttpServletRequest request) throws Exception{
       return userService.logout(request);
    }
}

