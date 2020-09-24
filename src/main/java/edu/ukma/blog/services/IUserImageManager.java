package edu.ukma.blog.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface IUserImageManager {
    String saveImage(MultipartFile original) throws IOException;

    Path getImage(String location);

    Path getMinImage(String location);
}
