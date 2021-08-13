package com.suzu.dao;

import com.suzu.pojo.UserMessage;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface MessageMapper {
	int insertOne(int messageToID, int messageSenderID, String message);
	int insertOneStrangerMessageToken(String token);
	int insertOneStrangerMessage(Integer tokenID, Integer messageSenderID, String message);
	int selectCountStrangerMessageToken(String token);
	int selectMessageTokenID(String token);
}
