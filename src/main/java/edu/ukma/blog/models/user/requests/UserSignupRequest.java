package edu.ukma.blog.models.user.requests;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class UserSignupRequest {

    @Pattern(regexp = "\\w{3,20}")
    private String username;

    // todo: think if it is possible to get the pattern from configuration file
    @Pattern(regexp = "^[\\w\\d@$!%*#?&]{5,25}$")
    private String password;

    private String status; // short description about user occupation

    private String description;
}
