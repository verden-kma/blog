package edu.ukma.blog.models.record;

import edu.ukma.blog.models.compositeIDs.RecordID;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class RecordEntity {
    // idea for a feature: look through all likers, dislikers, find influencers, save them separately
    @EmbeddedId
    private RecordID id;

    @NotEmpty
    private String caption;

    @NotEmpty
    private String imgLocation;

    private String adText;

    private String timestamp; // LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC) ?

    private boolean isEdited;

    @ElementCollection
    private List<Long> likeUsers;

    @ElementCollection
    private List<Long> dislikeUsers;

    @ElementCollection
    private List<Integer> comments;
}