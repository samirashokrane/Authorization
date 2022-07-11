package com.vispax.authorization.dto;


import lombok.Data;

import java.io.Serializable;


@Data
public class VerifyUserReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userName;

	private String activationCode;

}