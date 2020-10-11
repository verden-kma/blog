package edu.ukma.blog.models.record;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RequestRecord {
    private String caption;

    private MultipartFile image;

    private String adText;
}
