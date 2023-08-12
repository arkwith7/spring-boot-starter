package com.arkwith.starter.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> _member = userRepository.findByUsername(username);
        if (_member.isEmpty()) {
            throw new UsernameNotFoundException("사용자ID가 '" + username + "'인 사용자를 찾을수 없습니다.");
        }

        Member member = _member.get();
        List<GrantedAuthority> authorities = new ArrayList<>();

        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getRole()));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getRole()));
        }

        return new User(member.getUsername(), member.getPassword(), authorities);
        
    }

    
}
