package edu.ukma.blog.services;

import edu.ukma.blog.exceptions.ServerCriticalException;
import edu.ukma.blog.exceptions.WrongFileFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface IRecordImageService {
    String saveImage(MultipartFile original) throws ServerCriticalException, WrongFileFormatException;

    File getImage(String location);

    File getImageMin(String location);

    File getImageIcon(String location);
}
