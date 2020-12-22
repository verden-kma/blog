package edu.ukma.blog.repositories.graph_repos;

import edu.ukma.blog.models.simple_interaction.graph_models.RecordGraphEntity;
import edu.ukma.blog.repositories.graph_repos.graph_projections.RecordRecomView;
import edu.ukma.blog.repositories.graph_repos.graph_projections.RecordView;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IRecordNodesRepo extends Neo4jRepository<RecordGraphEntity, UUID> {
    @Query("MATCH (user:UserGraphEntity {userId: {0}}), (record:RecordGraphEntity {publisherId : {1}, recordOwnId : {2}}) " +
            "CREATE (user)-[:LIKES]->(record)")
    void setLike(long userId, long publisherId, int recordOwnId);

    @Query("MATCH (user:UserGraphEntity {userId: {0}}), (record:RecordGraphEntity {publisherId : {1}, recordOwnId : {2}}) " +
            "CREATE (user)-[:DISLIKES]->(record)")
    void setDislike(long userId, long publisherId, int recordOwnId);

    @Query("MATCH (user:UserGraphEntity {userId: {0}})-[rel]->(record:RecordGraphEntity {publisherId : {1}, recordOwnId : {2}}) DELETE rel")
    void unset(long userId, long publisherId, int recordOwnId);

    @Query("MATCH (users:UserGraphEntity)-[:LIKES]->(:RecordGraphEntity {publisherId : {0}, recordOwnId : {1}}),\n" +
            "(users)-[:LIKES]->(records:RecordGraphEntity)\n" +
            "RETURN records.publisherId AS publisherId, records.recordOwnId AS recordOwnId, COUNT(*) AS Strength ORDER BY Strength DESC")
    List<RecordView> getRecordRecomsSimilarToRecord(long publisherId, int recordOwnId, int limit);

    @Query("MATCH (target:UserGraphEntity {userId:{0}})-[:LIKES]->(likeRecs:RecordGraphEntity), " +
            "(simLikeUsr:UserGraphEntity)-[:LIKES]->(likeRecs), " +
            "(simLikeUsr)-[:LIKES]->(simLikeRecs:RecordGraphEntity)\n" +
            "WHERE simLikeRecs <> likeRecs AND NOT exists((target)-[:DISLIKES]->(simLikeRecs))\n" +
            "RETURN simLikeRecs.uuid AS uuid, simLikeRecs.publisherId AS publisherId, simLikeRecs.recordOwnId AS recordOwnId, " +
            "count(simLikeRecs) AS strength ORDER BY strength DESC LIMIT {1}")
    List<RecordRecomView> getRecordsRecoms(long userId, int limit);

    @Query("MATCH (target:UserGraphEntity {userId:{0}})-[:DISLIKES]->(dislikeRecs:RecordGraphEntity), " +
            "(simDislikeUsr:UserGraphEntity)-[:DISLIKES]->(dislikeRecs), " +
            "(simDislikeUsr)-[:DISLIKES]->(simDislikeRecs:RecordGraphEntity)\n" +
            "WHERE simDislikeRecs.uuid IN {1}\n" +
            "RETURN simDislikeRecs.publisherId AS publisherId, simDislikeRecs.recordOwnId AS recordOwnId, " +
            "count(simDislikeRecs) AS strength")
    List<RecordRecomView> getRecordCounterRecoms(long userId, Collection<String> recomCandidates);

    // Long or Iterable<Long> is required as the return type of a Delete query
//    Long deleteByPublisherIdAndRecordOwnId(long publisherId, int recordOwnId);

    @Modifying
    @Query("MATCH (r:RecordGraphEntity {publisherId : {0}, recordOwnId : {1}}) DETACH DELETE r")
    void deleteByPublisherIdAndRecordOwnId(long publisherId, int recordOwnId);
}
