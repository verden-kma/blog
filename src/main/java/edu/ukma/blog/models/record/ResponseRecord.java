package edu.ukma.blog.models.record;

import lombok.Data;

@Data
public class ResponseRecord {
    private String caption;
    private String timestamp;
    private int likes;
    private int dislikes;
    private int numOfComments;
}
