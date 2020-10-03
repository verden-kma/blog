package edu.ukma.blog.models.record;

import edu.ukma.blog.models.compositeIDs.RecordID;
import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class RecordEntity {
    // idea for feature: look through all likers, dislikers, find influencers, save them separately
    @EmbeddedId
    private RecordID id;

    private String caption;

    @NotEmpty
    private String imgLocation;

    private LocalDateTime timestamp;

    @ElementCollection
    private List<Long> likeUsers;

    @ElementCollection
    private List<Long> dislikeUsers;

    @ElementCollection
    private List<Integer> comments;
}