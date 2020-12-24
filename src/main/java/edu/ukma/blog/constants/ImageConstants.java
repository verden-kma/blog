package edu.ukma.blog.constants;

import lombok.Data;
import org.springframework.http.MediaType;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ImageConstants {
    public static final int AVATAR_SIZE = 200;
    public static final ImgDims TOP_BANNER_DIMS = new ImgDims(2000, 315);

    public static final String TARGET_MEDIA_TYPE = MediaType.IMAGE_JPEG_VALUE;
    public static final Set<String> ACCEPTABLE_MEDIA_TYPES = Collections.unmodifiableSet(new HashSet<>
            (Arrays.asList(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE)));

    public static final String TARGET_IMAGE_FORMAT = "jpg";
    public static final String[] ACCEPTABLE_FORMATS = new String[]{"jpg", "jpeg", "png", "gif"};

    public static final String PATH_PREFIX = new File("").getAbsolutePath().concat("/userImages");
    public static final int RECORD_ICON_SIZE = 100;
    public static final int FOLDERS_WIDTH = 128;

    @Data
    public static class ImgDims {
        private final int width;
        private final int height;
    }
}
