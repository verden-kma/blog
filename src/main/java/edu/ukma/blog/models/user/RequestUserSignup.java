package edu.ukma.blog.models.user;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class RequestUserSignup {

    @Pattern(regexp = "\\w{3,20}")
    private String username;

    @Pattern(regexp = "^[\\w\\d@$!%*#?&]{5,25}$")
    private String password;

    private String status; // short description about user occupation

    private String description;
}
