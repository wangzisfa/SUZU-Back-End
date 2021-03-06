package com.suzu.service;

import com.suzu.pojo.PlainUser;
import com.suzu.pojo.PlainUserDTO;
import com.suzu.pojo.UserConversationToken;

import java.util.List;

public interface UserService {
	Boolean login(PlainUserDTO user);
	Boolean register(PlainUserDTO user);
	PlainUser getProfile(String username);
	String updateProfileByUsername(PlainUser user, String originUsername);
	List<UserConversationToken> getFriends(String username);
	List<PlainUser> getAll(Integer listIndex, Integer listItemNum, String currentUsername);
	Integer subscribeOne(String username, String subscribeToUsername, String token);
	List<PlainUser> fuzzySearchByUsername(String username);
}
