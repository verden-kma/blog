package edu.ukma.blog.models.compositeIDs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CommentID implements Serializable {
    private long publisherId;

    private int recordId;

    private int commentId;
}
