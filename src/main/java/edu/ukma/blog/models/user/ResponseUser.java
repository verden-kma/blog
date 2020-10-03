package edu.ukma.blog.models.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResponseUser {
    @NotBlank
    private String username;

    private String status; // short description about user occupation

    private String description;

    private String avaPath;
}
