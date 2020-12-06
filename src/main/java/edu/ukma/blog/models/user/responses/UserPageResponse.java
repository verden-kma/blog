package edu.ukma.blog.models.user.responses;

import lombok.Data;

import java.util.List;

@Data
public class UserPageResponse {
    private String username;

    private String status; // short description about user's occupation

    private String description;

    private int uploads;

    private int followers;

    private int likes;

    private int dislikes;

    private int comments;

    private boolean isFollowed;

    private List<String> subscriptions;
}
