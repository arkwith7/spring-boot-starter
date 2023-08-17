package com.arkwith.starter.user;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(nullable = true)
    private String nickname;

    private String name;

    private String picture;

    private String password;

    @Column(unique = true)
    private String email;

    // OAuth2 provider;
    private String provider;
    private String providerId;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Role role;


    @Builder
    public Member(String username,String nickname, String name, String picture, String email, String provider, String providerId, Role role){
        this.username = username;
        this.nickname = nickname;
        this.name = name;
        this.picture = picture;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
        this.createDate = LocalDateTime.now();
    }

    public Member update(String username,String nickname, String name, String picture, String email, String provider, String providerId, Role role){
        this.username = username;
        this.nickname = nickname;
        this.name = name;
        this.picture = picture;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
        this.modifyDate = LocalDateTime.now();
        return this;
    }
    public String getRoleKey(){
        return "ROLE_USER";
    }

}
