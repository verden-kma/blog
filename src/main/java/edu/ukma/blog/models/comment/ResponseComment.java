package edu.ukma.blog.models.comment;

import edu.ukma.blog.models.compositeIDs.CommentID;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.validation.constraints.NotEmpty;

@Data
public class ResponseComment {
    private int commentId;

    private String commentator;

    private String text;

    private String timestamp;
}
