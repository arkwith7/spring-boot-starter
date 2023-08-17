package com.arkwith.starter.auth;

import com.arkwith.starter.user.Role;
import com.arkwith.starter.user.Member;

import lombok.Getter;

@Getter
public class SessionUser {
    private String username;
    private String nickname;
    private String name;
    private String picture;
    private Role role;
    private String email;

    public SessionUser(Member member){
        this.username = member.getUsername();
        this.nickname = member.getNickname();
        this.name = member.getName();
        this.picture = member.getPicture();
        this.role = member.getRole();

        this.email = member.getEmail();
    }
    
}
