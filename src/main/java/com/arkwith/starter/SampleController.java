package com.arkwith.starter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/sample")
public class SampleController {

    @GetMapping("/index")
    public String index() {
        return "pages/sample/index";
    }

    @GetMapping("/form")
    public String form() {
        return "pages/sample/form";
    }    
    
    @GetMapping("/ui")
    public String ui() {
        return "pages/sample/ui";
    }    

    @GetMapping("/tab")
    public String tab() {
        return "pages/sample/tab";
    }    

    @GetMapping("/table")
    public String table() {
        return "pages/sample/table";
    }    

    @GetMapping("/blank")
    public String blank() {
        return "pages/sample/blank";
    }    

    @GetMapping("/login")
    public String login() {
        return "pages/sample/login";
    }    

    @GetMapping("/signup")
    public String signup() {
        return "pages/sample/signup";
    }    
}
