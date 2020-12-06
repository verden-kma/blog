package edu.ukma.blog.controllers.communication;

import edu.ukma.blog.PropertyAccessor;
import edu.ukma.blog.SpringApplicationContext;
import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.models.record.evaluation.EvalPage;
import edu.ukma.blog.services.IRecordEvalService;
import edu.ukma.blog.services.IUserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users/{publisher}/records/{recordId}")
public class EvaluationCtrl {
    /*
     * user can load page on 2 devices (tabs) [null]
     * put like on d1, this like will not be displayed on d2 [true]
     * go to d2, put like again, this action should not remove like,
     * therefore there are separate put and delete endpoints
     * */

    private static final int EVAL_BLOCK_SIZE = ((PropertyAccessor) SpringApplicationContext
            .getBean(PropertyAccessor.PROPERTY_ACCESSOR_BEAN_NAME)).getEvalBlockSize();

    private final IRecordEvalService reactionService;

    private final IUserService userService;

    public EvaluationCtrl(IRecordEvalService reactionService, IUserService userService) {
        this.reactionService = reactionService;
        this.userService = userService;
    }

    @GetMapping("/likers")
    public EvalPage getLikers(@PathVariable String publisher,
                              @PathVariable int recordId,
                              @RequestParam int block) {
        long publisherId = userService.getUserId(publisher);
        return reactionService.getLikers(new RecordId(publisherId, recordId), PageRequest.of(block, EVAL_BLOCK_SIZE));
    }

    @PutMapping("/likers")
    public void likeRecord(@PathVariable String publisher,
                           @PathVariable int recordId,
                           Principal principal) {
        long publisherId = userService.getUserId(publisher);
        long userId = userService.getUserId(principal.getName());
        reactionService.putEvaluation(new RecordId(publisherId, recordId), userId, true);
    }

    @DeleteMapping("/likers")
    public void removeLike(@PathVariable String publisher,
                           @PathVariable int recordId,
                           Principal principal) {
        long publisherId = userService.getUserId(publisher);
        long userId = userService.getUserId(principal.getName());
        reactionService.removeEvaluation(new RecordId(publisherId, recordId), userId, true);
    }

    @GetMapping("/dislikers")
    public EvalPage getDislikers(@PathVariable String publisher,
                                 @PathVariable int recordId,
                                 @RequestParam int block) {
        long publisherId = userService.getUserId(publisher);
        return reactionService.getDislikers(new RecordId(publisherId, recordId), PageRequest.of(block, EVAL_BLOCK_SIZE));
    }

    @PutMapping("/dislikers")
    public void dislikeRecord(@PathVariable String publisher,
                              @PathVariable int recordId,
                              Principal principal) {
        long publisherId = userService.getUserId(publisher);
        long userId = userService.getUserId(principal.getName());
        reactionService.putEvaluation(new RecordId(publisherId, recordId), userId, false);
    }

    @DeleteMapping("/dislikers")
    public void removeDislike(@PathVariable String publisher,
                              @PathVariable int recordId,
                              Principal principal) {
        long publisherId = userService.getUserId(publisher);
        long userId = userService.getUserId(principal.getName());
        reactionService.removeEvaluation(new RecordId(publisherId, recordId), userId, false);
    }
}