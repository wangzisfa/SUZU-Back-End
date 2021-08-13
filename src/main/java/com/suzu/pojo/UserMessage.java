package com.suzu.pojo;

import lombok.Data;

@Data
public class UserMessage {
	private String message;
	private User userReceive;
	private PlainUser userSend;

	@Data
	public static class User {
		String token;
		PlainUser user;
		Integer userID;
	}
}
