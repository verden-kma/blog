package edu.ukma.blog.models.user.requests;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String username;

    private String password;
}
