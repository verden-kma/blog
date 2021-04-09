package edu.ukma.blog.models.user.requests;

import edu.ukma.blog.constants.PatternConstants;
import lombok.Data;

import javax.annotation.Nullable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Data
public class UserSignupRequest {
    @Email
    @NotNull
    private String email;

    @NotNull
    @Pattern(regexp = PatternConstants.USERNAME_PATTERN)
    private String username;

    @NotNull
    @Pattern(regexp = PatternConstants.PASSWORD_PATTERN)
    private CharSequence password;

    @Nullable
    private String status;

    @Nullable
    private String description;
}
