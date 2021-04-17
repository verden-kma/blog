package edu.ukma.blog.models.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseRecord {
    private String publisher;

    private int id;

    private String caption;

    private String adText;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @JsonProperty("isEdited")
    private boolean isEdited;

    // like - true, dislike - false, ignore - null
    private Boolean reaction;

    private int likes;

    private int dislikes;

    private int numOfComments;
}
