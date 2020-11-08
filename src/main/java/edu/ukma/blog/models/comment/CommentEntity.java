package edu.ukma.blog.models.comment;

import edu.ukma.blog.models.compositeIDs.CommentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity {
    @EmbeddedId
    private CommentId id;

    private long commentatorId;

    @NotEmpty
    private String text;

    private String timestamp;
}
