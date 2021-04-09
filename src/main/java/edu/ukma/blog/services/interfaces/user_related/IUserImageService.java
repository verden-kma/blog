package edu.ukma.blog.services.interfaces.user_related;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

public interface IUserImageService {
    Optional<File> getAvatar(String username);

    void setAvatar(MultipartFile image, String username);

    void removeAvatar(String username);

    Optional<File> getTopBanner(String username);

    void setTopBanner(MultipartFile image, String username);

    void removeTopBanner(String username);
}
