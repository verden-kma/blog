package edu.ukma.blog.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EvalPage {
    List<String> evaluators;
    boolean isLast;
}
