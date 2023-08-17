package com.arkwith.starter.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.arkwith.starter.DataNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입시 회원등록
    public Member create(String username, String password, String nickname, String name, String picture, String email) {
        Member member = new Member();
        member.setUsername(username);
        // BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        member.setPassword(passwordEncoder.encode(password));

        if (nickname == null) {
            member.setNickname(username);
        } else {
            member.setNickname(nickname);
        }

        if (name == null) {
            member.setName(username);
        } else {
            member.setName(name);
        }
        member.setPicture(picture);
        member.setEmail(email);
        member.setRole(Role.USER);
        member.setCreateDate(LocalDateTime.now());
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

    public Member findById(Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        });
    }

    public Page<Member> getList(int page, int numPerPage, String kw) {
        // Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, numPerPage, Sort.by(sorts));
        // Specification<Question> spec = search(kw);
        // return questionRepository.findAll(spec, pageable);
        return this.userRepository.findAllByKeyword(kw, pageable);
    }
}
