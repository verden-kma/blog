package edu.ukma.blog.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

public interface IUserImageService {
    Optional<File> getAvatar(long userId);

    void setAvatar(MultipartFile image, long userId);

    void removeAvatar(long userId);

    Optional<File> getMainPageImage(long userId);

    void setMainPageImage(MultipartFile image, long userId);

    void removeMainPageImage(long userId);
}
