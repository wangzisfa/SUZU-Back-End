package com.suzu.controller;


import com.alibaba.fastjson.JSONObject;
import com.suzu.pojo.PlainUser;
import com.suzu.pojo.PlainUserDTO;
import com.suzu.service.UserService;
import com.suzu.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class FriendController {
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
}
