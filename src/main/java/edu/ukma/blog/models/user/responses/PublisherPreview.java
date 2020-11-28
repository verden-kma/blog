package edu.ukma.blog.models.user.responses;

import lombok.Data;

import java.util.List;

@Data
public class PublisherPreview {
    private String username;

    private int followers;

    private int uploads;

    private List<String> lastRecordsImgPaths;
}
