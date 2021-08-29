package com.suzu.controller;

import com.alibaba.fastjson.JSONObject;
import com.suzu.pojo.PlainUserDTO;
import com.suzu.pojo.ResponseMessage;
import com.suzu.service.UserService;
import com.suzu.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://111.229.238.150:3000")
public class LoginController {
	@Autowired
	UserService service;

	@Autowired
	private final JwtTokenUtil tokenService;

	public LoginController(JwtTokenUtil tokenService) {
		this.tokenService = tokenService;
	}

	/*
	* token
	* */
	@PostMapping("/login")
	@ResponseBody
	public ResponseEntity<Object> login(@RequestBody JSONObject object) {
		System.out.println(object);
//		PlainUserDTO user = object.toJavaObject(PlainUserDTO.class);
		PlainUserDTO user = new PlainUserDTO();
		user.setPassword(object.getString("password"));
		user.setUsername(object.getString("username"));
		user.setRole(object.getString("role"));


		ResponseMessage message = new ResponseMessage();
		message.setAuthorization(tokenService.generateToken(user, true));
		message.setMessage(user.getRole());
		return service.login(user) ?
				new ResponseEntity<>(message, HttpStatus.OK) :
				new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/register")
	@ResponseBody
	public ResponseEntity<Object> register(@RequestBody JSONObject object) {
		PlainUserDTO user = object.toJavaObject(PlainUserDTO.class);
		System.out.println(user.toString());

		return service.register(user) ?
				new ResponseEntity<>("success", HttpStatus.OK) :
				new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/validateUser")
	@ResponseBody
	public ResponseEntity<Object> isUserTokenValidate(@RequestHeader("authorization") String token) {
		return tokenService.isTokenExpired(token) ?
				new ResponseEntity<>("token expired", HttpStatus.UNAUTHORIZED) :
				new ResponseEntity<>("success", HttpStatus.OK);
	}
}
