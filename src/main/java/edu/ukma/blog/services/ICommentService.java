package edu.ukma.blog.services;

import edu.ukma.blog.models.comment.ResponseComment;
import edu.ukma.blog.models.compositeIDs.CommentId;
import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.utils.LazyContentPage;
import org.springframework.data.domain.Pageable;

public interface ICommentService {
    int addComment(RecordId recordId, long commenterId, String text);

    LazyContentPage<ResponseComment> getCommentsBlock(long publisherId, RecordId recordId, Pageable pageable);

    void removeComment(CommentId commentID, long commentatorId);
}
