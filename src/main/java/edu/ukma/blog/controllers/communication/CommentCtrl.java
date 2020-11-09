package edu.ukma.blog.controllers.communication;

import com.google.common.collect.BiMap;
import edu.ukma.blog.PropertyAccessor;
import edu.ukma.blog.SpringApplicationContext;
import edu.ukma.blog.exceptions.comment.NoSuchCommentException;
import edu.ukma.blog.models.comment.CommentEntity;
import edu.ukma.blog.models.comment.CommentEntity_;
import edu.ukma.blog.models.comment.RequestComment;
import edu.ukma.blog.models.comment.ResponseComment;
import edu.ukma.blog.models.compositeIDs.CommentId;
import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.services.ICommentService;
import edu.ukma.blog.services.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{publisher}/records/{recordId}/comments")
public class CommentCtrl {
    @Autowired
    private ICommentService commentService;

    @Autowired
    private IUserService userService;

    private static final int COMMENTS_BLOCK_SIZE;

    static {
        String beanName = PropertyAccessor.class.getSimpleName();
        String propertyAccessorBeanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
        COMMENTS_BLOCK_SIZE = ((PropertyAccessor) SpringApplicationContext
                .getBean(propertyAccessorBeanName)).getCommentBlockSize();
    }

    /**
     * stores a comment in database, assigns it to publisher/record/nextComment
     *
     * @param publisher  - username of a content creator
     * @param recordId   - id of a record
     * @param newComment - core comment data
     * @return valid id of a new comment in a database
     */
    @PostMapping
    public int addComment(@PathVariable String publisher,
                          @PathVariable int recordId,
                          @RequestBody RequestComment newComment) {
        long publisherId = userService.getUserId(publisher);
        RecordId fullRecordId = new RecordId(publisherId, recordId);
        long commenterId = userService.getUserId(newComment.getCommenter());
        return commentService.addComment(fullRecordId, commenterId, newComment.getText());
    }

    @GetMapping
    public List<ResponseComment> getComments(@PathVariable String publisher,
                                             @PathVariable int recordId,
                                             @RequestParam int block) {
        long publisherId = userService.getUserId(publisher);
        Pageable pageable = PageRequest.of(block, COMMENTS_BLOCK_SIZE, Sort.by(CommentEntity_.TIMESTAMP).descending());
        List<CommentEntity> comments = commentService.getCommentsBlock(new RecordId(publisherId, recordId), pageable);
        return getResponseComments(comments);
    }

    @GetMapping(path = "/all")
    public List<ResponseComment> getComments(@PathVariable String publisher,
                                             @PathVariable int recordId) {
        long publisherId = userService.getUserId(publisher);
        List<CommentEntity> comments = commentService.getComments(new RecordId(publisherId, recordId));
        return getResponseComments(comments);
    }

    private List<ResponseComment> getResponseComments(List<CommentEntity> comments) {
        // different comments are written by different users, users can write multiple comments
        // all records are posted by 1 publisher
        List<Long> ids = comments.stream().map(CommentEntity::getCommentatorId).collect(Collectors.toList());
        final BiMap<Long, String> userIds = userService.getUserIdentifiersBimap(ids);

        return comments.stream().map(commentEntity -> {
            ResponseComment response = new ResponseComment();
            BeanUtils.copyProperties(commentEntity, response);
            response.setCommentId(commentEntity.getId().getCommentOwnId());
            response.setCommentator(userIds.get(commentEntity.getCommentatorId()));
            return response;
        }).collect(Collectors.toList());
    }

    @DeleteMapping(path = "/{commentId}")
    public void removeComment(@PathVariable String publisher,
                              @PathVariable int recordId,
                              @PathVariable int commentId) {
        long publisherId = userService.getUserId(publisher);
        try {
            commentService.removeComment(new CommentId(new RecordId(publisherId, recordId), commentId));
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchCommentException(publisher, recordId, commentId);
        }
    }
}
