package com.arkwith.starter.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@RestController
public class UserAvatarController {
    
    @Value("${upload.directory}")
    private String uploadDirectory;

    private final UserService userService;

    @GetMapping("/avatars/{memberId}")
    public ResponseEntity<Resource> getAvatar(@PathVariable Long memberId) {
        Member member = userService.findById(memberId);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }

        String avatarPath = member.getPicture();
        Path imagePath = Paths.get(uploadDirectory, avatarPath);

        try {
            Resource resource = new UrlResource(imagePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            // Handle error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}