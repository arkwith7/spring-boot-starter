package com.arkwith.starter.auth;

import com.arkwith.starter.user.Member;

import lombok.Getter;

@Getter
public class SessionUser {
    private String username;
    private String email;

    public SessionUser(Member member){
        this.username = member.getUsername();
        this.email = member.getEmail();
    }
    
}
