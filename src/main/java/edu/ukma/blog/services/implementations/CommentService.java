package edu.ukma.blog.services.implementations;

import edu.ukma.blog.models.comment.CommentEntity;
import edu.ukma.blog.models.comment.CommentEntity_;
import edu.ukma.blog.models.compositeIDs.CommentId;
import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.repositories.ICommentsRepo;
import edu.ukma.blog.repositories.IPublisherStatsRepo;
import edu.ukma.blog.services.ICommentService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService implements ICommentService {

    @PersistenceContext
    private EntityManager em;

    private final ICommentsRepo commentsRepo;

    private final IPublisherStatsRepo publisherStatsRepo;

    public CommentService(ICommentsRepo commentsRepo, IPublisherStatsRepo publisherStatsRepo) {
        this.commentsRepo = commentsRepo;
        this.publisherStatsRepo = publisherStatsRepo;
    }

    @Override
    public int addComment(RecordId recordId, long commenterId, String text) {
        Optional<CommentEntity> lastComment = commentsRepo.findTopById_RecordIdOrderById_CommentOwnIdDesc(recordId);
        CommentId commentId = new CommentId(recordId, lastComment
                .map(commentEntity -> commentEntity.getId().getCommentOwnId() + 1).orElse(1));
        CommentEntity comment = new CommentEntity(commentId, commenterId, text, Instant.now().toString());
        commentsRepo.save(comment);
        publisherStatsRepo.incCommentsCount(recordId.getPublisherId());
        return commentId.getCommentOwnId();
    }

    @Override
    public List<CommentEntity> getCommentsBlock(RecordId recordId, Pageable pageable) {
        return commentsRepo.findAllById_RecordId(recordId, pageable);
    }

    /**
     * deletes a comment denoted by the <code>commentId</code> if it is being deleted by a publisher of the comment
     * in the event of successful deletion, publisher statistics is updated
     *
     * @param commentID     compound primary key of CommentEntity stored in database
     * @param commentatorId id of a user who wants to delete a comment
     */
    @Override
    @Transactional
    public void removeComment(CommentId commentID, long commentatorId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<CommentEntity> criteriaDelete = cb.createCriteriaDelete(CommentEntity.class);
        Root<CommentEntity> root = criteriaDelete.from(CommentEntity.class);

        criteriaDelete.where(cb.equal(root.get(CommentEntity_.ID), commentID),
                cb.equal(root.get(CommentEntity_.COMMENTATOR_ID), commentatorId));
        boolean hasDeleted = em.createQuery(criteriaDelete).executeUpdate() == 1;

        if (hasDeleted) publisherStatsRepo.decCommentsCount(commentID.getRecordId().getPublisherId());
    }
}
