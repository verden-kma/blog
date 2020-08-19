package edu.ukma.blog.models;

import edu.ukma.blog.models.compositeIDs.CommentID;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Data
@Entity
public class Comment {
    @EmbeddedId
    private CommentID id;

    private String text;

    private LocalDateTime timestamp;
}
