package edu.ukma.blog.repositories;

import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.models.record.RecordEntity;
import edu.ukma.blog.repositories.projections.record.MinRecordView;
import edu.ukma.blog.repositories.projections.record.RecordImgLocationAndPublisherIdView;
import edu.ukma.blog.repositories.projections.record.RecordImgLocationView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    //todo: return RecordOwnIdView instead of RecordEntity
    Optional<RecordEntity> findTopById_PublisherIdOrderById_RecordOwnIdDesc(long publisherId);

    boolean existsByImgLocation(String location);

    @Query("SELECT record.imgLocation FROM RecordEntity record WHERE record.id=:id")
    Optional<String> getImgLocation(@Param("id") RecordId id);
//    Optional<RecordImgLocationView> findImgLocationById(RecordId id); //todo: test with projection

    int countAllById_PublisherId(long publisherId);

    List<RecordEntity> findAllById_PublisherId(long publisherId, Pageable pageable);

    List<RecordImgLocationView> findById_PublisherId(long publisherId, Pageable pageable);

    @Query(value = "SELECT publisher_id, img_location FROM (\n" +
            "    SELECT publisher_id, \n" +
            "           img_location, \n" +
            "           ROW_NUMBER() OVER (PARTITION BY publisher_id ORDER BY timestamp DESC) AS rec_rank \n" +
            "    FROM record_entity) ranks\n" +
            "WHERE rec_rank <= :depth AND publisher_id IN (:publisherIds);", nativeQuery = true)
    List<RecordImgLocationAndPublisherIdView> getLastRecordsOfPublishers(@Param("publisherIds") List<Long> publisherIds,
                                                                         @Param("depth") int depth);

    Slice<MinRecordView> findAllBy(Pageable pageable);

//    @Query("SELECT rec.isEdited FROM RecordEntity rec JOIN rec.likeUsers lius WHERE rec.id=:recordId AND lius=:likerId")
//    Boolean existsByLikeUsersIn(@Param("recordId") RecordId recordId, @Param("likerId") long likerId);

//    as an example to remember
//    @Transactional
//    @Modifying // try custom object, if fail then primitive types
//    @Query("UPDATE RecordEntity rec SET rec.caption=:title, rec.isEdited=true WHERE rec.id=:id")
//    void updateCaption(@Param("id") RecordID id, @Param("title") String title);
}
