package edu.ukma.blog.models.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RequestUserSignup {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    private String password;

    private String status; // short description about user occupation

    private String description;
}
