package edu.ukma.blog.models.composite_id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class FollowerId implements Serializable {
    private long publisherId;

    private long subscriberId;
}
