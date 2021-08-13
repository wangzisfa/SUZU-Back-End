package com.suzu.service.impl;

import com.suzu.dao.UserMapper;
import com.suzu.pojo.PlainUser;
import com.suzu.pojo.PlainUserDTO;
import com.suzu.pojo.UserConversationToken;
import com.suzu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class UserServiceImpl implements UserService {
	private final AtomicInteger UserListOffset = new AtomicInteger(0);


	@Autowired
	UserMapper mapper;

	@Autowired
	PasswordEncoder encoder;



	@Override
	public Boolean login(PlainUserDTO userDTO) {
		String encodedPassword = mapper.verityPassword(userDTO.getUsername());
		return encoder.matches(userDTO.getPassword(), encodedPassword);
	}

	@Override
	public Boolean register(PlainUserDTO userDTO) {
		PlainUser user = new PlainUser();
		user.setUsername(userDTO.getUsername());
		if (mapper.isUserRegistered(userDTO.getUsername()) == 1) {
			return false;
		} else {
			String encodingPassword = encoder.encode(userDTO.getPassword());
			System.out.println("register encoding password : " + encodingPassword);
			user.setPassword(encodingPassword);
			return mapper.register(user) == 1;
		}
	}

	@Override
	public PlainUser getProfile(String username) {
		PlainUser user = mapper.getProfileByUsername(username);
		if (user.getGender().equals("M")) {
			user.setGender("男");
		} else {
			user.setGender("女");
		}

		return user;
	}

	@Override
	public List<UserConversationToken> getFriends(String username) {
		System.out.println(username);
		List<UserConversationToken> list = mapper.getFriendsIDAndConversationToken(username);
		System.out.println(list);
		List<PlainUser> userList = new ArrayList<>();
		for (UserConversationToken userConversationToken : list) {
			userConversationToken.setUser(mapper.getProfileByID(userConversationToken.getUserID()));
		}

		System.out.println("current userConversationList is :    " + list);
		return list;
	}

	@Override
	public List<PlainUser> getAll(Integer listIndex, Integer num, String currentUsername) {
//		PageHelper.startPage(listIndex, 8);
		int startItemIndex = (listIndex - 1) * num;
		System.out.println(currentUsername);
		List<PlainUser> list = mapper.getUserList(startItemIndex, num, currentUsername);
//		System.out.println(list);
		return list;
	}

	@Override
	public Integer subscribeOne(String username, String subscribeToUsername, String token) {
		int userID = mapper.getUserIDByUsername(username);
		int subscribeID = mapper.getUserIDByUsername(subscribeToUsername);

		return mapper.insertSubscribeUser(userID, subscribeID, token);
	}

}
