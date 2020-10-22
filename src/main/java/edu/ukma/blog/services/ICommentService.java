package edu.ukma.blog.services;

import edu.ukma.blog.models.comment.CommentEntity;
import edu.ukma.blog.models.compositeIDs.CommentID;
import edu.ukma.blog.models.compositeIDs.RecordID;

import java.util.List;

public interface ICommentService {
    int addComment(RecordID recordId, long commenterId, String text);

    List<CommentEntity> getComments(RecordID recordId);

    void removeComment(CommentID commentID);
}
