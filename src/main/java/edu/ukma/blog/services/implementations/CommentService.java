package edu.ukma.blog.services.implementations;

import edu.ukma.blog.models.comment.CommentEntity;
import edu.ukma.blog.models.compositeIDs.CommentId;
import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.repositories.ICommentsRepo;
import edu.ukma.blog.repositories.IRecordsRepo;
import edu.ukma.blog.services.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService implements ICommentService {

    @Autowired
    private ICommentsRepo commentsRepo;

    @Autowired
    private IRecordsRepo recordsRepo;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public int addComment(RecordId recordId, long commenterId, String text) {
        Optional<CommentEntity> lastComment = commentsRepo.findTopById_RecordIdOrderById_CommentIdDesc(recordId);
        CommentId commentId = new CommentId(recordId, lastComment
                .map(commentEntity -> commentEntity.getId().getCommentId() + 1).orElse(1));
        CommentEntity comment = new CommentEntity(commentId, commenterId, text, Instant.now().toString());
        commentsRepo.save(comment);


        //todo
//        RecordEntity re = em.find(RecordEntity.class, recordId);
//        re.getComments().add(commentId.getCommentId());


        return commentId.getCommentId();
    }

    @Override
    public List<CommentEntity> getComments(RecordId recordId) {
        return commentsRepo.findAllById_RecordId(recordId);
    }

    @Override
    public void removeComment(CommentId commentID) {
        commentsRepo.deleteById(commentID);
    }
}
