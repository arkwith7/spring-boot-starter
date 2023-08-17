package com.arkwith.starter.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.arkwith.starter.user.Member;

public class PrincipalDetails implements UserDetails, OAuth2User{

    private static final long serialVersionUID = 1L;
    private Member member;
    private Map<String, Object> attributes;

    // 일반 Security 로그인시 사용
    public PrincipalDetails(Member member) {
        this.member = member;
    }

    // OAuth2 로그인시 사용
    public PrincipalDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    public Member getMember() {
        return member;
    }

    public String getRoleKey() {
        return member.getRoleKey();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() { // 우리 사이트에서 1년동안 회원이 로그인을 안하면 휴먼 계정으로 하기로 함
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collet = new ArrayList<>();
        collet.add(new SimpleGrantedAuthority(member.getRoleKey()));
        return collet;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return member.getName();
    }
    
}
