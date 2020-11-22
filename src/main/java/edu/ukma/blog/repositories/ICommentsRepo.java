package edu.ukma.blog.repositories;

import edu.ukma.blog.models.comment.CommentEntity;
import edu.ukma.blog.models.compositeIDs.CommentId;
import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.repositories.projections.record.RecordCommentsNumView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICommentsRepo extends JpaRepository<CommentEntity, CommentId> {
    Optional<CommentEntity> findTopById_RecordIdOrderById_CommentOwnIdDesc(RecordId recordId);

    int countAllById_RecordId(RecordId recordId);

    List<CommentEntity> findAllById_RecordId(RecordId recordId);

    List<CommentEntity> findAllById_RecordId(RecordId recordId, Pageable pageable);

    @Query(value = "SELECT \n" +
            "    record_own_id,\n" +
            "    COUNT(CONCAT(publisher_id, '-', record_own_id)) AS comment_count\n" +
            "FROM\n" +
            "    comment_entity\n" +
            "WHERE\n" +
            "    publisher_id = :publisherId\n" +
            "        AND record_own_id IN (:recordIds)\n" +
            "GROUP BY record_own_id", nativeQuery = true)
    List<RecordCommentsNumView> getCommentsNumForRecords(@Param("publisherId") long publisherId,
                                                         @Param("recordIds") List<Integer> recordIds);

    void deleteById_RecordId(RecordId recordId);
}
