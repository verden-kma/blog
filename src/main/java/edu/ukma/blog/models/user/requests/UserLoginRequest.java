package edu.ukma.blog.models.user.requests;

import edu.ukma.blog.constants.PatternConstants;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserLoginRequest {
    @Pattern(regexp = PatternConstants.USERNAME_PATTERN)
    private String username;

    @NotBlank
    private String password;
}
