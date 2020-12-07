package edu.ukma.blog.models.user.responses;

import lombok.Data;

import java.util.List;

@Data
public class UserDataPreviewResponse {
    private String publisherName;

    private int followers;

    private int uploads;

    boolean isFollowed;

    private List<Integer> lastRecords;
}
