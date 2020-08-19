package edu.ukma.blog.models;

import edu.ukma.blog.models.compositeIDs.RecordID;
import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;

// Basically on uploading the profile picture, rename it to something that is unique for that user. E.g. uid_001.jpg

@Data
@Entity
public class Record {

    @EmbeddedId
    private RecordID id;

    private String caption;

    private String imgPath;

    private LocalDateTime timestamp;

    @ElementCollection
    private List<Long> likeUsers;

    @ElementCollection
    private List<Long> dislikeUsers;

    @ElementCollection
    private List<Integer> comments;
}
