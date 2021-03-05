package edu.ukma.blog.models.user.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDataResponse {
    private String username;

    private String status; // short description about user's occupation

    private String description;

    private int uploads;

    private int followers;

    private int likes;

    private int dislikes;

    private int comments;

    @JsonProperty("isFollowed")
    private boolean isFollowed;
}
