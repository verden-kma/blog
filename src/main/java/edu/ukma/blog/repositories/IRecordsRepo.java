package edu.ukma.blog.repositories;

import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.models.record.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRecordsRepo extends JpaRepository<RecordEntity, RecordId> {
    /**
     * among all records associated with a publisher with <code>publisherId</code> get the record with the last recordId
     * (get last compound <code>RecordId</code>)
     *
     * @param publisherId - id of a user who has published a record
     * @return <code>Record</code> with the largest <code>recordId</code>
     */
    Optional<RecordEntity> findTopById_PublisherIdOrderById_RecordIdDesc(long publisherId);

    boolean existsByImgLocation(String location);

    @Query("SELECT record.imgLocation FROM RecordEntity record WHERE record.id=:id")
    Optional<String> getImgLocation(@Param("id") RecordId id);
//    Optional<String> findImgLocationById(RecordId id); //todo: test with projection


    //    @Query("SELECT CASE WHEN :likerId IN (SELECT rec.likeUsers FROM RecordEntity rec WHERE rec.id=:id) " +
//            "THEN TRUE ELSE FALSE END FROM ")
//    @Query("SELECT CASE WHEN :likerId IN (SELECT rec.likeUsers FROM RecordEntity rec WHERE rec.id=:recordId) " +
//            "THEN TRUE ELSE FALSE END FROM RecordEntity re")
//    boolean existsLiker(@Param("recordId") RecordId recordId, @Param("likerId") long likerId);

//    @Query("SELECT rec.isEdited FROM RecordEntity rec JOIN rec.likeUsers lius WHERE rec.id=:recordId AND lius=:likerId")
//    Boolean existsByLikeUsersIn(@Param("recordId") RecordId recordId, @Param("likerId") long likerId);

//    as an example to remember
//    @Transactional
//    @Modifying // try custom object, if fail then primitive types
//    @Query("UPDATE RecordEntity rec SET rec.caption=:title, rec.isEdited=true WHERE rec.id=:id")
//    void updateCaption(@Param("id") RecordID id, @Param("title") String title);
}
