package edu.ukma.blog.models.user.requests;

import edu.ukma.blog.constants.PatternConstants;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class EditUserPasswordRequest {
    @NotNull
    @Pattern(regexp = PatternConstants.PASSWORD_PATTERN)
    private CharSequence currentPassword;

    @NotNull
    @Pattern(regexp = PatternConstants.PASSWORD_PATTERN)
    private CharSequence newPassword;
}
