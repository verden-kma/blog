package edu.ukma.blog.repositories;

import edu.ukma.blog.models.comment.CommentEntity;
import edu.ukma.blog.models.compositeIDs.CommentId;
import edu.ukma.blog.models.compositeIDs.RecordId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICommentsRepo extends JpaRepository<CommentEntity, CommentId> {
    Optional<CommentEntity> findTopById_RecordIdOrderById_CommentIdDesc(RecordId recordId);

//    List<CommentEntity> findTop10ByCommentatorIdAndIdRecordIdOrderByTimestamp(long publisherId, int recordId);

    int countAllById_RecordId(RecordId recordId);

    List<CommentEntity> findAllById_RecordId(RecordId recordId);

    void deleteById_RecordId(RecordId recordId);
}
