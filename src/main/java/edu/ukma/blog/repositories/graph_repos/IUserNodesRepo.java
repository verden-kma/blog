package edu.ukma.blog.repositories.graph_repos;

import edu.ukma.blog.models.simple_interaction.graph_models.UserGraphEntity;
import edu.ukma.blog.repositories.graph_repos.graph_projections.UserRecomView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface IUserNodesRepo extends Neo4jRepository<UserGraphEntity, Long> {
    @Query("MATCH (subs:UserGraphEntity {userId: {0}}), (publ:UserGraphEntity {userId: {1}}) CREATE (subs)-[:FOLLOWS]->(publ)")
    void setFollow(long subscriberId, long publisherId);

    @Query("MATCH (subs:UserGraphEntity {userId: {0}})-[f:FOLLOWS]->(publ:UserGraphEntity {userId: {1}}) DELETE f")
    void setUnfollow(long subscriberId, long publisherId);

    @Query("MATCH (target:UserGraphEntity{userId:{0}})-[:FOLLOWS]->(publs:UserGraphEntity), " +
            "(similarUsr:UserGraphEntity)-[:FOLLOWS]->(publs:UserGraphEntity), " +
            "(similarUsr:UserGraphEntity)-[:FOLLOWS]->(similarPubls:UserGraphEntity) WHERE similarPubls <> publs " +
            "RETURN similarPubls.userId AS recommendation, COUNT(*) AS strength ORDER BY strength")
    List<UserRecomView> getRecommendations(long customerId, Pageable pageable);
}
