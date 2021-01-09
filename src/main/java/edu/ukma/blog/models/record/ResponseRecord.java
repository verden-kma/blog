package edu.ukma.blog.models.record;

import lombok.Data;

@Data
public class ResponseRecord {
    private String publisher;

    private int id;

    private String caption;

    private String adText;

    private String timestamp;

    private boolean isEdited;

    // like - true, dislike - false, ignore - null
    private Boolean reaction;

    private int likes;

    private int dislikes;

    private int numOfComments;
}
