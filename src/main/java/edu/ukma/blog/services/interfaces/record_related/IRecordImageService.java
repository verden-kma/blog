package edu.ukma.blog.services.interfaces.record_related;

import edu.ukma.blog.exceptions.server_internal.ServerCriticalError;
import edu.ukma.blog.exceptions.server_internal.WrongFileFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface IRecordImageService {
    String saveImage(MultipartFile original) throws ServerCriticalError, WrongFileFormatException;

    File getImage(String location);

    File getImageMin(String location);

    File getImageIcon(String location);

    void deleteImage(String location);
}
