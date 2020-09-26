package edu.ukma.blog.services;

import edu.ukma.blog.exceptions.ServerError;
import edu.ukma.blog.exceptions.WrongFileFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface IUserImageManager {
    String saveImage(MultipartFile original) throws ServerError, WrongFileFormatException;

    Path getImage(String location);

    Path getMinImage(String location);
}
