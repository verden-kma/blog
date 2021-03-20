package edu.ukma.blog.models.comment;

import edu.ukma.blog.models.composite_id.CommentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

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

    private LocalDateTime timestamp;
}
