package edu.ukma.blog.models.simple_interaction.graph_models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
@NoArgsConstructor
@Data
public class UserGraphEntity {
    @Id
    long userId;
    @Relationship(type = "FOLLOWS")
    private Set<UserGraphEntity> followers;
    @Relationship(type = "LIKES")
    private Set<RecordGraphEntity> likedRecords;
    @Relationship(type = "DISLIKES")
    private Set<RecordGraphEntity> dislikedRecords;

    public UserGraphEntity(long id) {
        userId = id;
        followers = new HashSet<>();
        likedRecords = new HashSet<>();
        dislikedRecords = new HashSet<>();
    }
}
