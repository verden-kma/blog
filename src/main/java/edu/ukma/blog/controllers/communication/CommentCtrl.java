package edu.ukma.blog.controllers.communication;

import edu.ukma.blog.exceptions.comment.NoSuchCommentException;
import edu.ukma.blog.models.comment.CommentEntity_;
import edu.ukma.blog.models.comment.RequestComment;
import edu.ukma.blog.models.comment.ResponseComment;
import edu.ukma.blog.models.composite_id.CommentId;
import edu.ukma.blog.models.composite_id.RecordId;
import edu.ukma.blog.services.ICommentService;
import edu.ukma.blog.services.IUserService;
import edu.ukma.blog.utils.LazyContentPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;

@RestController
@RequestMapping("/users/{publisher}/records/{recordId}/comments")
@RequiredArgsConstructor
public class CommentCtrl {
    @Value("${commentsPerBlock}")
    private final int COMMENTS_BLOCK_SIZE;
//            = ((PropertyAccessor) SpringApplicationContext
//            .getBean(PropertyAccessor.PROPERTY_ACCESSOR_BEAN_NAME)).getCommentBlockSize();

    private final ICommentService commentService;

    private final IUserService userService;

    /**
     * stores a comment in database, assigns it to publisher/record/nextComment
     *
     * @param publisher  - username of a content creator
     * @param recordId   - id of a record
     * @param newComment - core comment data
     * @return valid id of a new comment in a database
     */
    @PostMapping
    public int addComment(@PathVariable @NotEmpty String publisher,
                          @PathVariable @Min(1) int recordId,
                          @RequestBody @Valid RequestComment newComment) {
        long publisherId = userService.getUserIdByUsername(publisher);
        RecordId fullRecordId = new RecordId(publisherId, recordId);
        long commenterId = userService.getUserIdByUsername(newComment.getCommenter());
        return commentService.addComment(fullRecordId, commenterId, newComment.getText());
    }

    @GetMapping
    public LazyContentPage<ResponseComment> getComments(@PathVariable @NotEmpty String publisher,
                                                        @PathVariable @Min(1) int recordId,
                                                        @RequestParam @Min(0) int block) {
        long publisherId = userService.getUserIdByUsername(publisher);
        Pageable pageable = PageRequest.of(block, COMMENTS_BLOCK_SIZE, Sort.by(CommentEntity_.TIMESTAMP).descending());
        return commentService.getCommentsBlock(publisherId, new RecordId(publisherId, recordId), pageable);
    }

    @DeleteMapping(path = "/{commentId}")
    public void removeComment(@PathVariable @NotEmpty String publisher,
                              @PathVariable @Min(1) int recordId,
                              @PathVariable @Min(1) int commentId,
                              Principal principal) {
        long publisherId = userService.getUserIdByUsername(publisher);
        long userId = userService.getUserIdByUsername(principal.getName());
        try {
            commentService.removeComment(new CommentId(new RecordId(publisherId, recordId), commentId), userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchCommentException(publisher, recordId, commentId);
        }
    }
}
