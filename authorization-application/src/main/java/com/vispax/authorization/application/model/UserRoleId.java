package com.vispax.authorization.application.model;


import lombok.Data;

import java.io.Serializable;

@Data
public class UserRoleId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long userId;

	private Integer roleId;
	

}