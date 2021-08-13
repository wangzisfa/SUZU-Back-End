package com.suzu.service.impl;

import com.suzu.dao.MessageMapper;
import com.suzu.dao.UserMapper;
import com.suzu.pojo.StrangerMessage;
import com.suzu.pojo.UserMessage;
import com.suzu.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	private MessageMapper messageMapper;

	@Autowired
	private UserMapper userMapper;

	@Override
	public int insertOne(UserMessage message) {
		int messageSenderID = userMapper.getUserIDByUsername(message.getUserSend().getUsername());
		return messageMapper.insertOne(message.getUserReceive().getUserID(), messageSenderID, message.getMessage());
	}

	@Override
	public int insertOneStrangerMessageToken(String token) {
		if (messageMapper.selectCountStrangerMessageToken(token) == 1) {
			return 0;
		} else {
			return messageMapper.insertOneStrangerMessageToken(token) == 1 ? 1 : -1;
		}
	}

	@Override
	public int insertOneStrangerMessage(StrangerMessage message) {
		int senderID = userMapper.getUserIDByUsername(message.getMessageSenderName());
		int tokenID = messageMapper.selectMessageTokenID(message.getToken());
		return messageMapper.insertOneStrangerMessage(tokenID, senderID, message.getMessage());
	}
}
