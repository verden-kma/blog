package edu.ukma.blog.repositories.graph_repos;

import edu.ukma.blog.models.simple_interaction.graph_models.RecordGraphEntity;
import edu.ukma.blog.repositories.graph_repos.graph_projections.RecordRecomView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;
import java.util.UUID;

//todo: remove record -> DETACH DELETE all evals // already there?
public interface IRecordNodesRepo extends Neo4jRepository<RecordGraphEntity, UUID> {
    @Query("MATCH (user:UserGraphEntity {userId: {0}}), (record:RecordGraphEntity {publisherId : {1}, recordOwnId : {2}}) " +
            "CREATE (user)-[:LIKES]->(record)")
    void setLike(long userId, long publisherId, int recordOwnId);

    @Query("MATCH (user:UserGraphEntity {userId: {0}}), (record:RecordGraphEntity {publisherId : {1}, recordOwnId : {2}}) " +
            "CREATE (user)-[:DISLIKES]->(record)")
    void setDislike(long userId, long publisherId, int recordOwnId);

    @Query("MATCH (user:UserGraphEntity {userId: {0}})-[rel]->(record:RecordGraphEntity {publisherId : {1}, recordOwnId : {2}}) DELETE rel")
    void unset(long userId, long publisherId, int recordOwnId);

    @Query("MATCH (target:UserGraphEntity {userId:{0}})-[:LIKES]->(likeRecs:RecordGraphEntity), " +
            "(simLikeUsr:UserGraphEntity)-[:LIKES]->(likeRecs:RecordGraphEntity), " +
            "(simLikeUsr:UserGraphEntity)-[:LIKES]->(simLikeRecs:RecordGraphEntity)\n" +
            "WHERE simLikeRecs <> likeRecs\n" +
            "RETURN simLikeRecs.publisherId AS publisherId, simLikeRecs.recordOwnId AS recordOwnId, " +
            "count(simLikeRecs) AS strength ORDER BY strength DESC")
    List<RecordRecomView> getRecordsRecoms(long userId, Pageable pageable);

//    @Query("MATCH (target:User {userId:{0}})-[:DISLIKES]->(dislikeRecs:Record), " +
//            "(simDislikeUsr:User)-[:DISLIKES]->(dislikeRecs:Record), " +
//            "(simDislikeUsr:User)-[:DISLIKES]->(simDislikeRecs:Record)\n" +
//            "WHERE simDislikeRecs <> dislikeRecs AND simDislikeRecs.recordId IN {1}\n" +
//            "RETURN simDislikeRecs.publisherId AS publisherId, simDislikeRecs.recordOwnId AS recordOwnId, " +
//            "count(simDislikeRecs) AS strength ORDER BY strength DESC")
//    List<RecordRecomView> getRecordCounterRecoms(long userId, Collection<RecordId> recomCandidates, Pageable pageable);

    void deleteByPublisherIdAndRecordOwnId(long publisherId, int recordOwnId);
}
