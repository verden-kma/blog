package edu.ukma.blog.models.user.requests;

import edu.ukma.blog.constants.PatternConstants;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class UserSignupRequest {

    @Pattern(regexp = "\\w{3,20}")
    private String username;

    @Pattern(regexp = PatternConstants.PASSWORD_PATTERN)
    private String password;

    private String status; // short description about user occupation

    private String description;
}
