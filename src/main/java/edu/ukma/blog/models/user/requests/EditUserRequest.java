package edu.ukma.blog.models.user.requests;

import edu.ukma.blog.constants.PatternConstants;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Pattern;

@Data
public class EditUserRequest {
    @Nullable
    @Pattern(regexp = PatternConstants.PASSWORD_PATTERN)
    private CharSequence password;

    @Nullable
    private String status;

    @Nullable
    private String description;
}
