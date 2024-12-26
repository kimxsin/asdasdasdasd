package com.ohgiraffers.semiproject.messenger.controller;

import com.ohgiraffers.semiproject.main.model.dto.UserInfoResponse;
import com.ohgiraffers.semiproject.main.model.service.UserInfoService;
import com.ohgiraffers.semiproject.messenger.model.dto.ChatDTO;
import com.ohgiraffers.semiproject.messenger.model.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Timestamp;

@Controller
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService service;
    private final UserInfoService userInfoService;

    @Autowired
    public ChatController(UserInfoService userInfoService){
        this.userInfoService = userInfoService;
    }

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public ChatDTO sendMessage(ChatDTO chat) {

        // 사용자 정보 조회
        UserInfoResponse info = userInfoService.getUserInfo();

        System.out.println("info = " + info);


        String code = info.getCode();
        System.out.println("code = " + code);

        chat.setSenderCode(code);
        chat.setTimestamp(new Timestamp(System.currentTimeMillis())); // 현재 시간 설정
        System.out.println("chat = " + chat);

        // 메시지 저장
        service.insertChat(chat);

        return chat; // 메시지 브로커를 통해 전송
    }
}
