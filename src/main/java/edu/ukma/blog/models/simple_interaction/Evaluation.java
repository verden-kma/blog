package edu.ukma.blog.models.simple_interaction;

import edu.ukma.blog.models.composite_id.EvaluatorId;
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
    private Boolean isLiker;
}
