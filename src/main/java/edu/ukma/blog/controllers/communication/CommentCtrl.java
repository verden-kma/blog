package edu.ukma.blog.controllers.communication;

import com.google.common.collect.BiMap;
import edu.ukma.blog.exceptions.comment.NoSuchCommentException;
import edu.ukma.blog.models.comment.CommentEntity;
import edu.ukma.blog.models.comment.RequestComment;
import edu.ukma.blog.models.comment.ResponseComment;
import edu.ukma.blog.models.compositeIDs.CommentID;
import edu.ukma.blog.models.compositeIDs.RecordID;
import edu.ukma.blog.services.ICommentService;
import edu.ukma.blog.services.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
        RecordID fullRecordId = new RecordID(publisherId, recordId);
        long commenterId = userService.getUserId(newComment.getCommenter());
        return commentService.addComment(fullRecordId, commenterId, newComment.getText());
    }

    @GetMapping
    public List<ResponseComment> getComments(@PathVariable String publisher,
                                             @PathVariable int recordId) {
        long publisherId = userService.getUserId(publisher);
        List<CommentEntity> comments = commentService.getComments(new RecordID(publisherId, recordId));
        List<Long> ids = comments.stream().map(CommentEntity::getCommentatorId).collect(Collectors.toList());
        final BiMap<Long, String> userIds = userService.getUserIdentifiersBimap(ids);

        return comments.stream().map(commentEntity -> {
            ResponseComment response = new ResponseComment();
            BeanUtils.copyProperties(commentEntity, response);
            response.setCommentId(commentEntity.getId().getCommentId());
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
            commentService.removeComment(new CommentID(publisherId, recordId, commentId));
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchCommentException(publisher, recordId, commentId);
        }
    }
}
