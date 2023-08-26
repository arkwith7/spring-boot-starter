package com.arkwith.starter.chatgpt;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chatgpt")
public class ChatController {

    @RequestMapping("/chat")
    public String chat() {
        return "pages/chatgpt/chatting";
    }
    
}
