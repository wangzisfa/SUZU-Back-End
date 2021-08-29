package com.suzu.controller;


import com.alibaba.fastjson.JSONObject;
import com.suzu.pojo.PlainUser;
import com.suzu.pojo.PlainUserDTO;
import com.suzu.service.UserService;
import com.suzu.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class SubscribeController {
	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenUtil tokenService;

	@GetMapping("/getUserList")
	@ResponseBody
	public List<PlainUser> getUserList(@RequestParam("listIndex") int listIndex,
	                                   @RequestParam(value = "num", defaultValue = "10") int pageItemNum,
	                                   @RequestParam("currentUsername") String username) {
		System.out.println(listIndex + username);

		return userService.getAll(listIndex, pageItemNum, username);
	}

	@GetMapping("/conversationToken")
	@ResponseBody
	public String getConversationToken(@RequestParam("username") String username) {
		PlainUserDTO user = new PlainUserDTO();
		user.setUsername(username);

		return tokenService.generateToken(user, false);
	}

	@GetMapping("/subscribeUser")
	@ResponseBody
	public ResponseEntity<Object> subscribeOne(@RequestParam("subscribeName") String username,
	                                           @RequestParam("subscribeTo") String subscribeToUsername) {
		PlainUserDTO user = new PlainUserDTO();
		user.setUsername(username);
		String token = tokenService.generateToken(user, false);
		System.out.println("token   " + token);

		System.out.println(username + "        " + subscribeToUsername);
		return userService.subscribeOne(username, subscribeToUsername, token) == 1 ?
				new ResponseEntity<>("success", HttpStatus.OK) :
				new ResponseEntity<>("bad request", HttpStatus.BAD_REQUEST);

	}


	@GetMapping("/search")
	@ResponseBody
	public List<PlainUser> fuzzySearchByUsername(@RequestParam("username") String username) {
		return userService.fuzzySearchByUsername(username);
	}
}
