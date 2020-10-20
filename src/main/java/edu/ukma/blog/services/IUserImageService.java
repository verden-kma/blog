package edu.ukma.blog.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

public interface IUserImageService {
    Optional<File> getAvatar(long userId);

    void setAvatar(MultipartFile image, long userId);

    void removeAvatar(long userId);

    Optional<File> getTopBanner(long userId);

    void setTopBanner(MultipartFile image, long userId);

    void removeTopBanner(long userId);
}
