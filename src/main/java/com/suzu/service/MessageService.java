package com.suzu.service;

import com.suzu.pojo.StrangerMessage;
import com.suzu.pojo.UserMessage;

public interface MessageService {
	int insertOne(UserMessage message);
	/*
	* 返回: 1 插入成功
	*      0 token存在, 不需要插入
	* */
	int insertOneStrangerMessageToken(String token);
	int insertOneStrangerMessage(StrangerMessage message);
}
