package edu.ukma.blog.models.comment;

import edu.ukma.blog.constants.ValidationConstants;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
public class RequestComment {
    @NotBlank(message = "commenter must be known")
    private String commenter;

    @NotBlank(message = "comment cannot be empty")
    @Size(max = ValidationConstants.MAX_COMMENT_LENGTH)
    private String text;
}
