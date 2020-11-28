package edu.ukma.blog.models.record;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MinResponseRecord {
    private String publisher;
    private long recordOwnId;
    private String caption;
    private String imgLocation;
}
