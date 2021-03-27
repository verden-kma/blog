package edu.ukma.blog.models.user.requests;

import edu.ukma.blog.constants.PatternConstants;
import lombok.Data;

import javax.validation.constraints.Pattern;


@Data
public class UserSignupRequest {

//    @Email
//    private String email;

    @Pattern(regexp = PatternConstants.USERNAME_PATTERN)
    private String username;

    @Pattern(regexp = PatternConstants.PASSWORD_PATTERN)
    private CharSequence password;

    private String status; // short description about user occupation

    private String description;
}
