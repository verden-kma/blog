package edu.ukma.blog.services;

import edu.ukma.blog.models.comment.CommentEntity;
import edu.ukma.blog.models.compositeIDs.CommentId;
import edu.ukma.blog.models.compositeIDs.RecordId;

import java.util.List;

public interface ICommentService {
    int addComment(RecordId recordId, long commenterId, String text);

    List<CommentEntity> getComments(RecordId recordId);

    void removeComment(CommentId commentID);
}
