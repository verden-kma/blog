package edu.ukma.blog.models.comment;

import lombok.Data;

@Data
public class ResponseComment {
    private int commentId;

    private String commentator;

    private String text;

    private String timestamp;
}
