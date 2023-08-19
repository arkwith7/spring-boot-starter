package com.arkwith.starter.user;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class AvatarImageService {

    private final Path avatarImageStoragePath;

    public AvatarImageService(String avatarImageStoragePath) {
        this.avatarImageStoragePath = Path.of(avatarImageStoragePath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.avatarImageStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create avatar image storage directory", e);
        }
    }

    public String storeAvatarImage(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        Path targetPath = this.avatarImageStoragePath.resolve(fileName);
        try {
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not store the avatar image", e);
        }
        return fileName;
    }

    public URI getAvatarImageUri(String fileName) {
        return ((Path) ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/avatars/")
                .path(fileName))
                .toUri();
    }
}
