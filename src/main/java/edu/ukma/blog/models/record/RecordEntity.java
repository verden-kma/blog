package edu.ukma.blog.models.record;

import edu.ukma.blog.models.composite_id.RecordId;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
public class RecordEntity {
    @EmbeddedId
    private RecordId id;

    @NotEmpty
    private String caption;

    @NotEmpty
    private String imgLocation;

    private String adText;

    private String timestamp; // LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC) ?

    private boolean isEdited;
}