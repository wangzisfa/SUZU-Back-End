package com.suzu.service.impl;

import com.suzu.dao.MessageMapping;
import com.suzu.dao.UserMapper;
import com.suzu.pojo.UserMessage;
import com.suzu.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	private MessageMapping messageMapping;

	@Autowired
	private UserMapper userMapper;

	@Override
	public int insertOne(UserMessage message) {
		int messageSenderID = userMapper.getUserIDByUsername(message.getUserSend().getUsername());
		return messageMapping.insertOne(message.getUserReceive().getUserID(), messageSenderID, message.getMessage());
	}
}
