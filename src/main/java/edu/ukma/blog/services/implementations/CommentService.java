package edu.ukma.blog.services.implementations;

import edu.ukma.blog.models.comment.CommentEntity;
import edu.ukma.blog.models.compositeIDs.CommentID;
import edu.ukma.blog.models.compositeIDs.RecordID;
import edu.ukma.blog.repositories.ICommentsRepo;
import edu.ukma.blog.services.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService implements ICommentService {

    @Autowired
    private ICommentsRepo commentsRepo;

    @Override
    public int addComment(RecordID recordId, long commenterId, String text) {
        Optional<CommentEntity> lastComment = commentsRepo.findTopById_PublisherIdAndId_RecordIdOrderById_CommentIdDesc
                (recordId.getPublisherId(), recordId.getRecordId());
        CommentID commentId = new CommentID(recordId.getPublisherId(), recordId.getRecordId(),
                lastComment.map(commentEntity -> commentEntity.getId().getCommentId() + 1).orElse(1));
        CommentEntity comment = new CommentEntity(commentId, commenterId, text, Instant.now().toString());
        commentsRepo.save(comment);
        return commentId.getCommentId();
    }

    @Override
    public List<CommentEntity> getComments(RecordID recordId) {
        return commentsRepo.findAllById_PublisherIdAndId_RecordId(recordId.getPublisherId(), recordId.getRecordId());
    }

    @Override
    public void removeComment(CommentID commentID) {
        commentsRepo.deleteById(commentID);
    }
}
