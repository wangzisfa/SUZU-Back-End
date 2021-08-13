package com.suzu.controller;


import com.suzu.pojo.PlainUser;
import com.suzu.pojo.UserConversationToken;
import com.suzu.service.UserService;
import com.suzu.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class UserProfileController {
	@Autowired
	private UserService service;
	@Autowired
	private JwtTokenUtil tokenService;

	@ResponseBody
	@GetMapping("/profile")
	public ResponseEntity<Object> getProfile(@RequestHeader("auth") String token) {
		System.out.println(token);
		String username = tokenService.getUsernameFromToken(token);
		System.out.println(username);
		PlainUser user = service.getProfile(username);
		user.setUsername(username);
		System.out.println(user);
		return new ResponseEntity<Object>(user, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping("/friends")
	public ResponseEntity<Object> getFriends(@RequestHeader("auth") String token) {
		String username = tokenService.getUsernameFromToken(token);
		List<UserConversationToken> list = service.getFriends(username);
		System.out.println(list.toString());
		return new ResponseEntity<Object>(list, HttpStatus.OK);
	}
}
