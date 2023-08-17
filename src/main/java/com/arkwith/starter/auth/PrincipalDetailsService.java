package com.arkwith.starter.auth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.arkwith.starter.user.Member;
import com.arkwith.starter.user.Role;
import com.arkwith.starter.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 일반 로그인시 사용하는 사용자 정보 적재 함수
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> _member = userRepository.findByUsername(username);
        if (_member.isEmpty()) {
            throw new UsernameNotFoundException("사용자ID가 '" + username + "'인 사용자를 찾을수 없습니다.");
        }

        Member member = _member.get();
        List<GrantedAuthority> authorities = new ArrayList<>();

        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getKey()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.USER.getKey()));
        }

        return new PrincipalDetails(member);
        
    }
    
}