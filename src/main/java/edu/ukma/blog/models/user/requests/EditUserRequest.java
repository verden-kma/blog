package edu.ukma.blog.models.user.requests;

import edu.ukma.blog.constants.PatternConstants;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class EditUserRequest {
    @Pattern(regexp = PatternConstants.PASSWORD_PATTERN)
    private String password;

    private String status;

    private String description;
}
