package edu.ukma.blog.repositories.graph_repos;

import edu.ukma.blog.models.simple_interaction.graph_models.UserGraphEntity;
import edu.ukma.blog.repositories.graph_repos.graph_projections.UserRecomView;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IUserNodesRepo extends Neo4jRepository<UserGraphEntity, Long> {
    @Query("MATCH (subs:UserGraphEntity {userId: $subscriberId}), (publ:UserGraphEntity {userId: $publisherId}) CREATE (subs)-[:FOLLOWS]->(publ)")
    void setFollow(@Param("subscriberId") Long subscriberId, @Param("publisherId") Long publisherId);

    @Query("MATCH (subs:UserGraphEntity {userId: $subscriberId})-[f:FOLLOWS]->(publ:UserGraphEntity {userId: $publisherId}) DELETE f")
    void setUnfollow(@Param("subscriberId") Long subscriberId, @Param("publisherId") Long publisherId);

    @Query("MATCH (target:UserGraphEntity{userId:$customerId})-[:FOLLOWS]->(publs:UserGraphEntity), " +
            "(similarUsr:UserGraphEntity)-[:FOLLOWS]->(publs:UserGraphEntity), " +
            "(similarUsr:UserGraphEntity)-[:FOLLOWS]->(similarPubls:UserGraphEntity) WHERE similarPubls <> publs " +
            "RETURN similarPubls.userId AS recommendation, COUNT(*) AS strength ORDER BY strength LIMIT $limit")
    List<UserRecomView> getRecommendations(@Param("customerId") Long customerId, @Param("limit") Integer limit);
}
