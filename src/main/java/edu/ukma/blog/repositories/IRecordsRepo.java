package edu.ukma.blog.repositories;

import edu.ukma.blog.models.compositeIDs.RecordID;
import edu.ukma.blog.models.record.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRecordsRepo extends JpaRepository<RecordEntity, RecordID> {
    /**
     * among all records associated with a publisher with <code>publisherId</code> get the record with the last recordId
     *
     * @param publisherId - id of a user who has published a record
     * @return <code>Record</code> with the largest <code>recordId</code>
     */
    Optional<RecordEntity> findTopByIdPublisherIdOrderByIdRecordIdDesc(long publisherId);

    boolean existsByImgLocation(String location);
}
