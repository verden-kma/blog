package edu.ukma.blog.models.comment;

import lombok.Data;

@Data
public class RequestComment {
    private String commenter;

    private String text;
}
