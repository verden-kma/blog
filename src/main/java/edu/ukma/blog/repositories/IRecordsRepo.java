package edu.ukma.blog.repositories;

import edu.ukma.blog.models.compositeIDs.RecordID;
import edu.ukma.blog.models.record.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface IRecordsRepo extends JpaRepository<RecordEntity, RecordID> {
    /**
     * among all records associated with a publisher with <code>publisherId</code> get the record with the last recordId
     *(get last compound <code>RecordId</code>)
     * @param publisherId - id of a user who has published a record
     * @return <code>Record</code> with the largest <code>recordId</code>
     */
    Optional<RecordEntity> findTopById_PublisherIdOrderById_RecordIdDesc(long publisherId);

    boolean existsByImgLocation(String location);

    @Query("SELECT record.imgLocation FROM RecordEntity record WHERE record.id=:id")
    Optional<String> getImgLocation(@Param("id") RecordID id);

    @Transactional
    @Modifying // try custom object, if fail then primitive types
    @Query("UPDATE RecordEntity rec SET rec.caption=:title, rec.isEdited=true WHERE rec.id=:id")
    void updateCaption(@Param("id") RecordID id, @Param("title") String title);

    @Transactional
    @Modifying
    @Query("UPDATE RecordEntity rec SET rec.adText=:adText, rec.isEdited=true WHERE rec.id=:id")
    void updateAdText(@Param("id") RecordID id, @Param("adText") String adText);

    @Transactional
    @Modifying
    @Query("UPDATE RecordEntity rec SET rec.caption=:title, rec.adText=:adText, rec.isEdited=true WHERE rec.id=:id")
    void updateRecord(@Param("id") RecordID id, @Param("title") String title, @Param("adText") String adText);
}
