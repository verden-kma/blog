package edu.ukma.blog.repositories;

import edu.ukma.blog.models.Comment;
import edu.ukma.blog.models.compositeIDs.CommentID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICommentsRepo extends JpaRepository<Comment, CommentID> {
    List<Comment> findTop10ByIdUserIdAndIdRecordIdOrderByTimestamp(long publisherId, int recordId);
}
