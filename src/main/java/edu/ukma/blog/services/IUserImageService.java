package edu.ukma.blog.services;

import edu.ukma.blog.exceptions.ServerError;
import edu.ukma.blog.exceptions.WrongFileFormatException;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;

public interface IUserImageService {
    String TARGET_MEDIA_TYPE = MediaType.IMAGE_JPEG_VALUE;
    String TARGET_IMAGE_FORMAT = "jpg";
    String[] ACCEPTABLE_FORMATS = new String[]{"jpg", "jpeg", "png", "bmp"};

    String saveImage(MultipartFile original) throws ServerError, WrongFileFormatException;

    File getImage(String location);

    File getImageMin(String location);
}
