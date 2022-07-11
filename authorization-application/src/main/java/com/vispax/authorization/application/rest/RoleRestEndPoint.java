package com.vispax.authorization.application.rest;

import com.vispax.authorization.application.service.RoleService;
import com.vispax.authorization.dto.user.response.RoleList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;


@RestController
@RequestMapping(value = "/boot")
@CrossOrigin(origins = "*")
public class RoleRestEndPoint implements Serializable {
    private static final Logger logger = LogManager.getLogger(RoleRestEndPoint.class);


    @Autowired
    RoleService roleService;


    @ResponseBody
    @GetMapping("/roles")
    public ResponseEntity<RoleList> allRoles(HttpServletRequest request) throws Exception {
        return new ResponseEntity<RoleList>(roleService.findAll(), HttpStatus.OK);
    }

}

