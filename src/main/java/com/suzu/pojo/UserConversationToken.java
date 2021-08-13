package com.suzu.pojo;


import lombok.Data;

@Data
public class UserConversationToken {
	private PlainUser user;
	private int userID;
	private String token;
}
