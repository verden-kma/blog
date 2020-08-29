package edu.ukma.blog.models;

import edu.ukma.blog.models.compositeIDs.CommentID;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
public class Comment {
    @EmbeddedId
    private CommentID id;

    @NotEmpty
    private String text;

    @NotNull
    private LocalDateTime timestamp;
}
