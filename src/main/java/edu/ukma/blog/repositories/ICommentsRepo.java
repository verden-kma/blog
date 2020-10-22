package edu.ukma.blog.repositories;

import edu.ukma.blog.models.comment.CommentEntity;
import edu.ukma.blog.models.compositeIDs.CommentID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICommentsRepo extends JpaRepository<CommentEntity, CommentID> {
    Optional<CommentEntity> findTopById_PublisherIdAndId_RecordIdOrderById_CommentIdDesc(long publisherId, int recordId);

//    List<CommentEntity> findTop10ByCommentatorIdAndIdRecordIdOrderByTimestamp(long publisherId, int recordId);

    List<CommentEntity> findAllById_PublisherIdAndId_RecordId(long publisherId, int recordId);

    void deleteById_PublisherIdAndId_RecordId(long userId, int recordId);
}
