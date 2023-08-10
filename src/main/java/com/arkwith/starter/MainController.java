package com.arkwith.starter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String main() {

        return "pages/index";
    }

    @GetMapping("/login")
    public String login() {
        return "pages/login";
    }    

    @GetMapping("/signup")
    public String signup() {
        return "pages/signup";
    }       

    @GetMapping("/test")
    public String qna() {
         return "pages/qna/test";
    }       
}
