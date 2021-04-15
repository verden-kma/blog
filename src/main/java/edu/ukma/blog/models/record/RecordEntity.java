package edu.ukma.blog.models.record;

import edu.ukma.blog.models.composite_id.RecordId;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

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

    private LocalDateTime timestamp;

    private Boolean isEdited;
}