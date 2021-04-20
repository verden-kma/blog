package edu.ukma.blog.models.user.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserDataPreviewResponse {
    private String publisher;

    private int followers;

    private int uploads;

    @JsonProperty("isFollowed")
    boolean isFollowed;

    private List<Integer> lastRecords;
}
