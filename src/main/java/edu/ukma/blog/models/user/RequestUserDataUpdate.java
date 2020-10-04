package edu.ukma.blog.models.user;

import lombok.Data;

@Data
public class RequestUserDataUpdate {
    private String encryptedPassword;

    private String status;

    private String description;
}
