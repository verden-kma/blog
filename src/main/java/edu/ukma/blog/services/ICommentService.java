package edu.ukma.blog.services;

import edu.ukma.blog.models.comment.CommentEntity;
import edu.ukma.blog.models.compositeIDs.CommentId;
import edu.ukma.blog.models.compositeIDs.RecordId;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICommentService {
    int addComment(RecordId recordId, long commenterId, String text);

    List<CommentEntity> getComments(RecordId recordId);

    List<CommentEntity> getCommentsBlock(RecordId recordId, Pageable pageable);

    void removeComment(CommentId commentID);
}
