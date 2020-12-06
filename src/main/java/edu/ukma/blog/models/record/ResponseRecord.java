package edu.ukma.blog.models.record;

import lombok.Data;

import javax.annotation.Nullable;

//todo: maybe it is a better idea to return a location of the image associated with a record and not perform additional query?
@Data
public class ResponseRecord {
    private int id;

    private String caption;

    private String adText;

    private String timestamp;

    private boolean isEdited;

    private String imgLocation; // todo: add img path to older code

    @Nullable // like - true, dislike - false, ignore - null
    private Boolean reaction; // used for current user

    private int likes;

    private int dislikes;

    private int numOfComments;
}
