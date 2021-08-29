package com.suzu.controller;


import com.alibaba.fastjson.JSONObject;
import com.suzu.pojo.StrangerMessage;
import com.suzu.pojo.UserMessage;
import com.suzu.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@Controller
//@CrossOrigin("*")
public class UserMessageController {
	@Autowired
	private SimpMessagingTemplate template;


	@Autowired
	private MessageService service;
	/*
	* 思路就是 前端把消息返回回来, 存在数据库里, 然后订阅了当前message节点的用户就能收到消息
	* 怎么订阅当前节点呢
	* 每次用户登录的时候前端先从数据库中请求好友列表
	* 当打开对应好友界面的时候 websocket 订阅当前好友
	* */
	@MessageMapping("channel/{token}")
	@SendTo("/topic/channel/{token}")
	public String sendMessage(@DestinationVariable String token, String message) {
//		template.convertAndSend("/topic/user/message/" + );
//.WebSocketAnnotationMethodMessageHandler : No matching message handler methods.
//		System.out.println("message: " + message + "\n current subs" + subscriberUsername);
		System.out.println("token:     " + token);
//		System.out.println("message:      " + message);

		JSONObject object = JSONObject.parseObject(message);

		if (object.getBoolean("stranger") != null) {
			StrangerMessage strangerMessage = new StrangerMessage();
			strangerMessage.setMessage(object.getString("message"));
			strangerMessage.setMessageSenderName(object.getString("messageSender"));
			strangerMessage.setToken(token);

			System.out.println(strangerMessage);
			System.out.println("陌生人聊天模式");

			int messageTokenInsertStatus = service.insertOneStrangerMessageToken(token);
			if (messageTokenInsertStatus == -1) {
				System.out.println("陌生人聊天 token 插入时出现问题");
			} else {
				if (service.insertOneStrangerMessage(strangerMessage) == 1) {
					System.out.println("一条陌生人聊天消息插入成功");
				} else {
					System.out.println("陌生人聊天消息插入失败");
				}
			}
		} else {
			UserMessage userMessage = object.toJavaObject(UserMessage.class);
			if (service.insertOne(userMessage) == 1) {
				System.out.println("数据库插入成功");
			} else {
				System.out.println("插入好像有点问题");
			}
		}



		return message;
	}
}
