package edu.ukma.blog.constants;

import org.springframework.http.MediaType;

import java.io.File;

public class ImageConstants {
    public static final String TARGET_MEDIA_TYPE = MediaType.IMAGE_JPEG_VALUE;
    public static final String TARGET_IMAGE_FORMAT = "jpg";
    public static final String[] ACCEPTABLE_FORMATS = new String[]{"jpg", "jpeg", "png", "bmp"};

    public static final String PATH_PREFIX = new File("").getAbsolutePath().concat("/userImages");
    public static final int ICON_SIZE = 100;
    public static final int FOLDERS_WIDTH = 128;
}
