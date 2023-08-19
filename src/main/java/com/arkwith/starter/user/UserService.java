package com.arkwith.starter.user;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.arkwith.starter.DataNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${upload.directory}")
    private String uploadDir;

    // 회원가입시 회원등록
    public Member create(String username, String password, String nickname, String name, String picture, Role role, String email) {
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
        member.setRole(role);
        
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

    public void save(Member member) {
        userRepository.save(member);
    }

    public void delete(Member member) {
        userRepository.delete(member);
    }

    public void changePassword(Member member, String password) {
        member.setPassword(passwordEncoder.encode(password));
        userRepository.save(member);
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path filePath = Paths.get(uploadDir, fileName);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return fileName;
    }

    public void deleteFile(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir, fileName);
        Files.deleteIfExists(filePath);
    }
}
