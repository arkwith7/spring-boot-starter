package com.arkwith.starter.user;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.arkwith.starter.DataNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(String username, String password, String email) {
        Member member = new Member();
        member.setUsername(username);
        // BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        member.setPassword(passwordEncoder.encode(password));
        member.setEmail(email);
        userRepository.save(member);

        return member;
    }
    
    public Member getUser(String username) {
        Optional<Member> memberUser = this.userRepository.findByUsername(username);
        if (memberUser.isPresent()) {
            return memberUser.get();
        } else {
            throw new DataNotFoundException("member not found");
        }
    }
}
