package edu.ukma.blog.models;

import edu.ukma.blog.models.compositeIDs.FollowerId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Follower {
    @EmbeddedId
    private FollowerId id;
}
