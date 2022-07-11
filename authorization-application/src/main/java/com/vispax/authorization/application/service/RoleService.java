package com.vispax.authorization.application.service;

import com.vispax.authorization.dto.user.response.RoleList;

import java.io.Serializable;

public interface RoleService extends Serializable{

	RoleList findAll();

}
