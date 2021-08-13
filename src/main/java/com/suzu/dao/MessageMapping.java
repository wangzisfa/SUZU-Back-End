package com.suzu.dao;

import com.suzu.pojo.UserMessage;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface MessageMapping {
	int insertOne(int messageToID, int messageSenderID, String message);
}
