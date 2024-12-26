package com.ohgiraffers.semiproject.messenger.model.dao;

import com.ohgiraffers.semiproject.messenger.model.dto.ChatDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMapper {


    void save(ChatDTO chat);

//    List<Chat> findByChatList(@Param("sender") String sender, @Param("receiver") String receiver);
}