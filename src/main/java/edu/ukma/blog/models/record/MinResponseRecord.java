package edu.ukma.blog.models.record;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MinResponseRecord {
    private String publisher;

    private int recordOwnId;

    private String caption;
}
