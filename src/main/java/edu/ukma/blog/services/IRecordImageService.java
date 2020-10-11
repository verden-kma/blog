package edu.ukma.blog.services;

import edu.ukma.blog.exceptions.server_internal.ServerCriticalError;
import edu.ukma.blog.exceptions.server_internal.WrongFileFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

// works with binary (image files) data only
public interface IRecordImageService {
    String saveImage(MultipartFile original) throws ServerCriticalError, WrongFileFormatException;

    File getImage(String location);

    File getImageMin(String location);

    File getImageIcon(String location);

    boolean deleteImage(String location);
}
