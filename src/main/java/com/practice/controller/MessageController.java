package com.practice.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class MessageController {


    @MessageMapping("/messages")
    @SendTo(value = "/topic/message")
    public String sendMessage(String message) {
        return message;
    }

}
