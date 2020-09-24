package edu.ukma.blog.models.user;

import lombok.Data;

@Data
public class RequestUserLogin {
    private String username;

    private String password;
}
