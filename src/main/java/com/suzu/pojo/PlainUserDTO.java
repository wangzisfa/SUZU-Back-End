package com.suzu.pojo;


import lombok.Data;

/*
* 前端传过来是 PlainUserDTO
* 后端传回去是 PlainUser
* */

@Data
public class PlainUserDTO {
	private String username;
	private String password;
	private String role;
}
