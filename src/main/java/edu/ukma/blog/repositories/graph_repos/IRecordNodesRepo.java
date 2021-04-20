package edu.ukma.blog.repositories.graph_repos;

import edu.ukma.blog.models.simple_interaction.graph_models.RecordGraphEntity;
import edu.ukma.blog.repositories.graph_repos.graph_projections.RecordRecomView;
import edu.ukma.blog.repositories.graph_repos.graph_projections.RecordView;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IRecordNodesRepo extends Neo4jRepository<RecordGraphEntity, UUID> {
    @Query("MATCH (user:UserGraphEntity {userId: $userId}), (record:RecordGraphEntity {publisherId : $publisherId, recordOwnId : $recordOwnId}) " +
            "CREATE (user)-[:LIKES]->(record)")
    void setLike(@Param("userId") long userId, @Param("publisherId") long publisherId, @Param("recordOwnId") int recordOwnId);

    @Query("MATCH (user:UserGraphEntity {userId: userId}), (record:RecordGraphEntity {publisherId : $publisherId, recordOwnId : $recordOwnId}) " +
            "CREATE (user)-[:DISLIKES]->(record)")
    void setDislike(@Param("userId") long userId, @Param("publisherId") long publisherId, @Param("recordOwnId") int recordOwnId);

    @Query("MATCH (user:UserGraphEntity {userId: $userId})-[rel]->(record:RecordGraphEntity {publisherId : $publisherId, recordOwnId : $recordOwnId}) DELETE rel")
    void unset(@Param("userId") long userId, @Param("publisherId") long publisherId, @Param("recordOwnId") int recordOwnId);

    @Query("MATCH (users:UserGraphEntity)-[:LIKES]->(:RecordGraphEntity {publisherId : $publisherId, recordOwnId : $recordOwnId}),\n" +
            "(users)-[:LIKES]->(records:RecordGraphEntity)\n" +
            "WHERE records.publisherId <> $userId AND NOT exists ((:UserGraphEntity {userId : $userId})-[:LIKES]->(records))\n" +
            "RETURN records.publisherId AS publisherId, records.recordOwnId AS recordOwnId, COUNT(*) AS Strength ORDER BY Strength DESC LIMIT $limit")
    List<RecordView> getRecordRecomsSimilarToRecord(@Param("publisherId") long publisherId, @Param("recordOwnId") int recordOwnId, @Param("userId") long userId, @Param("") int limit);

    @Query("MATCH (target:UserGraphEntity {userId:$userId})-[:LIKES]->(likeRecs:RecordGraphEntity), " +
            "(simLikeUsr:UserGraphEntity)-[:LIKES]->(likeRecs), " +
            "(simLikeUsr)-[:LIKES]->(simLikeRecs:RecordGraphEntity)\n" +
            "WHERE simLikeRecs <> likeRecs AND NOT exists((target)-[:DISLIKES]->(simLikeRecs))\n" +
            "RETURN simLikeRecs.uuid AS uuid, simLikeRecs.publisherId AS publisherId, simLikeRecs.recordOwnId AS recordOwnId, " +
            "count(simLikeRecs) AS strength ORDER BY strength DESC LIMIT limit")
    List<RecordRecomView> getRecordsRecoms(@Param("userId") long userId, @Param("limit") int limit);

    @Query("MATCH (target:UserGraphEntity {userId:$userId})-[:DISLIKES]->(dislikeRecs:RecordGraphEntity), " +
            "(simDislikeUsr:UserGraphEntity)-[:DISLIKES]->(dislikeRecs), " +
            "(simDislikeUsr)-[:DISLIKES]->(simDislikeRecs:RecordGraphEntity)\n" +
            "WHERE simDislikeRecs.uuid IN $recomCandidates\n" +
            "RETURN simDislikeRecs.publisherId AS publisherId, simDislikeRecs.recordOwnId AS recordOwnId, " +
            "count(simDislikeRecs) AS strength")
    List<RecordRecomView> getRecordCounterRecoms(@Param("userId") long userId, @Param("recomCandidates") Collection<String> recomCandidates);

    @Modifying
    @Query("MATCH (r:RecordGraphEntity {publisherId : $publisherId, recordOwnId : $recordOwnId}) DETACH DELETE r")
    void deleteByPublisherIdAndRecordOwnId(@Param("publisherId") long publisherId, @Param("recordOwnId") int recordOwnId);

//     Long or Iterable<Long> is required as the return type of a Delete query
//     but return value is never user
//    void deleteByPublisherIdAndRecordOwnId(long publisherId, int recordOwnId);

}
