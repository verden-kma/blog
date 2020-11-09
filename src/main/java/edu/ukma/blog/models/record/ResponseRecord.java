package edu.ukma.blog.models.record;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class ResponseRecord {
    private int id;

    private String caption;

    private String adText;

    private String timestamp;

    private boolean isEdited;

    @Nullable // like - true, dislike - false, ignore - null
    private Boolean reaction; // used for current user

    private int likes;

    private int dislikes;

    private int numOfComments;
}
