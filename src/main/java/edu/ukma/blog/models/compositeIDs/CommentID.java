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
    private String publisherId;

    private int recordId;

    private String userId;

    private int commentId;
}
