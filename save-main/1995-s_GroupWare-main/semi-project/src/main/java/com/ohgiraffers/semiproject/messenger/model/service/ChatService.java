package com.ohgiraffers.semiproject.messenger.model.service;

import com.ohgiraffers.semiproject.messenger.model.dao.ChatMapper;
import com.ohgiraffers.semiproject.messenger.model.dto.ChatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatMapper mapper;

    public void insertChat(ChatDTO chat) {

        mapper.save(chat);
    }

//    public List<Chat> findBySenderCodeAndReceiverCode(String sender, String receiver) {
//
//        return mapper.findByChatList(sender, receiver);
//
//    }
}
