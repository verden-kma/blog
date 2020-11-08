package edu.ukma.blog.models;

import edu.ukma.blog.models.compositeIDs.FollowerId;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Data
@Entity
public class Follower {
    @EmbeddedId
    private FollowerId id;
}
