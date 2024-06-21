package com.yp.CXOJ.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * @author 余炎培
 * @since 2024-06-09 星期日 14:28:03
 */
@Controller
public class WebSocketController {

    @MessageMapping("/sendData")
    @SendTo("/topic/data")
    public String sendData(String message) {
        return message;
    }
}
