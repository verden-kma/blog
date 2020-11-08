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
public class EvaluatorId implements Serializable {
    private RecordId recordId;
    private long evaluatorId;
}
