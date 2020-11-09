package edu.ukma.blog.models.record.evaluation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvalPage {
    List<String> evaluators;
    boolean isLast;
}
