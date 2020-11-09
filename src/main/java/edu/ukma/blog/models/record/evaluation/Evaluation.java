package edu.ukma.blog.models.record.evaluation;

import edu.ukma.blog.models.compositeIDs.EvaluatorId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {
    @EmbeddedId
    private EvaluatorId id;
    private Boolean isLiker; //  object for 'Example.of' findAll option
}
