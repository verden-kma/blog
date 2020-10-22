package edu.ukma.blog.models.comment;

import edu.ukma.blog.models.compositeIDs.CommentID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity {
    @EmbeddedId
    private CommentID id;

    private long commentatorId;

    @NotEmpty
    private String text;

    private String timestamp;
}
