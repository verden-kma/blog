package edu.ukma.blog.services.implementations;

import com.google.common.collect.BiMap;
import edu.ukma.blog.models.comment.CommentEntity;
import edu.ukma.blog.models.comment.CommentEntity_;
import edu.ukma.blog.models.comment.ResponseComment;
import edu.ukma.blog.models.composite_id.CommentId;
import edu.ukma.blog.models.composite_id.RecordId;
import edu.ukma.blog.repositories.ICommentsRepo;
import edu.ukma.blog.repositories.IPublisherStatsRepo;
import edu.ukma.blog.services.ICommentService;
import edu.ukma.blog.services.IUserService;
import edu.ukma.blog.utils.LazyContentPage;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService implements ICommentService {

    @PersistenceContext
    private EntityManager em;

    private final ICommentsRepo commentsRepo;

    private final IPublisherStatsRepo publisherStatsRepo;

    private final IUserService userService;

    public CommentService(ICommentsRepo commentsRepo, IPublisherStatsRepo publisherStatsRepo, IUserService userService) {
        this.commentsRepo = commentsRepo;
        this.publisherStatsRepo = publisherStatsRepo;
        this.userService = userService;
    }


    @Override
    @Transactional
    public int addComment(RecordId recordId, long commenterId, String text) {
        Optional<CommentEntity> lastComment = commentsRepo.findTopById_RecordIdOrderById_CommentOwnIdDesc(recordId);
        CommentId commentId = new CommentId(recordId, lastComment
                .map(commentEntity -> commentEntity.getId().getCommentOwnId() + 1).orElse(1));
        CommentEntity comment = new CommentEntity(commentId, commenterId, text, LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        commentsRepo.save(comment);
        publisherStatsRepo.incCommentsCount(recordId.getPublisherId());
        return commentId.getCommentOwnId();
    }

    @Override
    public LazyContentPage<ResponseComment> getCommentsBlock(long publisherId, RecordId recordId, Pageable pageable) {
        Slice<CommentEntity> comments = commentsRepo.findAllById_RecordId(recordId, pageable);

        // different comments are written by different users, users can write multiple comments
        // all records are posted by 1 publisher
        List<Long> ids = comments.stream().map(CommentEntity::getCommentatorId).collect(Collectors.toList());
        final BiMap<Long, String> userIds = userService.getUserIdentifiersBimap(ids);

        List<ResponseComment> resp = comments.stream().map(commentEntity -> {
            ResponseComment response = new ResponseComment();
            BeanUtils.copyProperties(commentEntity, response);
            response.setCommentId(commentEntity.getId().getCommentOwnId());
            response.setCommentator(userIds.get(commentEntity.getCommentatorId()));
            return response;
        }).collect(Collectors.toList());
        return new LazyContentPage<>(resp, comments.isLast());
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
