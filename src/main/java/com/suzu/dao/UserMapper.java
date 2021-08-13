package com.suzu.dao;


import com.suzu.pojo.PlainUser;
import com.suzu.pojo.UserConversationToken;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserMapper {
	int login(PlainUser user);
	int isUserRegistered(String username);
	int register(PlainUser user);
	String verityPassword(String username);
	PlainUser getProfileByUsername(String username);
	PlainUser getProfileByID(Integer userID);
	List<UserConversationToken> getFriendsIDAndConversationToken(String username);
//	去掉当前用户的用户列表
	List<PlainUser> getUserList(Integer listIndex, Integer num, String currentUsername);
	int getUserIDByUsername(String username);
}
