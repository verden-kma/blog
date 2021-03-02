package edu.ukma.blog.controllers.communication;

import edu.ukma.blog.models.composite_id.RecordId;
import edu.ukma.blog.services.IRecordEvalService;
import edu.ukma.blog.services.IUserService;
import edu.ukma.blog.utils.LazyContentPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;

@RestController
@RequestMapping("/users/{publisher}/records/{recordId}")
@RequiredArgsConstructor
public class EvaluationCtrl {
    /*
     * user can load page on 2 devices (tabs) [null]
     * put like on d1, this like will not be displayed on d2 [true]
     * go to d2, put like again, this action should not remove like,
     * therefore there are separate put and delete endpoints
     * */

    @Value("${evaluatorsPerBlock}")
    private final int EVAL_BLOCK_SIZE;
//            = ((PropertyAccessor) SpringApplicationContext
//            .getBean(PropertyAccessor.PROPERTY_ACCESSOR_BEAN_NAME)).getEvalBlockSize();

    private final IRecordEvalService reactionService;

    private final IUserService userService;

    @GetMapping("/likers")
    public LazyContentPage<String> getLikers(@PathVariable @NotEmpty String publisher,
                                             @PathVariable @Min(1) int recordId,
                                             @RequestParam @Min(0) int block) {
        long publisherId = userService.getUserIdByUsername(publisher);
        return reactionService.getLikers(new RecordId(publisherId, recordId), PageRequest.of(block, EVAL_BLOCK_SIZE));
    }

    @PutMapping("/likers")
    public void likeRecord(@PathVariable @NotEmpty String publisher,
                           @PathVariable @Min(1) int recordId,
                           Principal principal) {
        long publisherId = userService.getUserIdByUsername(publisher);
        long userId = userService.getUserIdByUsername(principal.getName());
        reactionService.putEvaluation(new RecordId(publisherId, recordId), userId, true);
    }

    @DeleteMapping("/likers")
    public void removeLike(@PathVariable @NotEmpty String publisher,
                           @PathVariable @Min(1) int recordId,
                           Principal principal) {
        long publisherId = userService.getUserIdByUsername(publisher);
        long userId = userService.getUserIdByUsername(principal.getName());
        reactionService.removeEvaluation(new RecordId(publisherId, recordId), userId, true);
    }

    @GetMapping("/dislikers")
    public LazyContentPage<String> getDislikers(@PathVariable @NotEmpty String publisher,
                                                @PathVariable @Min(1) int recordId,
                                                @RequestParam @Min(0) int block) {
        long publisherId = userService.getUserIdByUsername(publisher);
        return reactionService.getDislikers(new RecordId(publisherId, recordId), PageRequest.of(block, EVAL_BLOCK_SIZE));
    }

    @PutMapping("/dislikers")
    public void dislikeRecord(@PathVariable @NotEmpty String publisher,
                              @PathVariable @Min(1) int recordId,
                              Principal principal) {
        long publisherId = userService.getUserIdByUsername(publisher);
        long userId = userService.getUserIdByUsername(principal.getName());
        reactionService.putEvaluation(new RecordId(publisherId, recordId), userId, false);
    }

    @DeleteMapping("/dislikers")
    public void removeDislike(@PathVariable @NotEmpty String publisher,
                              @PathVariable @Min(1) int recordId,
                              Principal principal) {
        long publisherId = userService.getUserIdByUsername(publisher);
        long userId = userService.getUserIdByUsername(principal.getName());
        reactionService.removeEvaluation(new RecordId(publisherId, recordId), userId, false);
    }
}