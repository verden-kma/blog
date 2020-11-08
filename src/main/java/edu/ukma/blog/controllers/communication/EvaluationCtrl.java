package edu.ukma.blog.controllers.communication;

import edu.ukma.blog.PropertyAccessor;
import edu.ukma.blog.SpringApplicationContext;
import edu.ukma.blog.models.EvalPage;
import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.services.IRecordEvalService;
import edu.ukma.blog.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{publisher}/records/{recordId}")
public class EvaluationCtrl {
    /*
     * user can load page on 2 devices (tabs) [null]
     * put like on d1, this like will not be displayed on d2 [true]
     * go to d2, put like again, this action should not remove like,
     * therefore there are separate put and delete endpoints
     * */

    private static final int EVAL_BLOCK_SIZE;

    static {
        String beanName = PropertyAccessor.class.getSimpleName();
        String propertyAccessorBeanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
        EVAL_BLOCK_SIZE = ((PropertyAccessor) SpringApplicationContext
                .getBean(propertyAccessorBeanName)).getEvalBlockSize();
    }

    @Autowired
    private IRecordEvalService reactionService;

    @Autowired
    private IUserService userService;


    @GetMapping("/likers")
    public EvalPage getLikers(@PathVariable String publisher,
                              @PathVariable int recordId,
                              @RequestParam int block) {
        long publisherId = userService.getUserId(publisher);
        return reactionService.getLikers(new RecordId(publisherId, recordId), PageRequest.of(block, EVAL_BLOCK_SIZE));
    }

    @PutMapping("/likers/{username}")
    public void likeRecord(@PathVariable String publisher,
                           @PathVariable int recordId,
                           @PathVariable String username) {
        long publisherId = userService.getUserId(publisher);
        long userId = userService.getUserId(username);
        reactionService.putLike(new RecordId(publisherId, recordId), userId);
    }

    @DeleteMapping("/likers/{username}")
    public void removeLike(@PathVariable String publisher,
                           @PathVariable int recordId,
                           @PathVariable String username) {
        long publisherId = userService.getUserId(publisher);
        long userId = userService.getUserId(username);
        reactionService.removeLike(new RecordId(publisherId, recordId), userId);
    }

    @GetMapping("/dislikers")
    public EvalPage getDislikers(@PathVariable String publisher,
                                 @PathVariable int recordId,
                                 @RequestParam int block) {
        long publisherId = userService.getUserId(publisher);
        return reactionService.getDislikers(new RecordId(publisherId, recordId), PageRequest.of(block, EVAL_BLOCK_SIZE));
    }

    @PutMapping("/dislikers/{username}")
    public void dislikeRecord(@PathVariable String publisher,
                              @PathVariable int recordId,
                              @PathVariable String username) {
        long publisherId = userService.getUserId(publisher);
        long userId = userService.getUserId(username);
        reactionService.putDislike(new RecordId(publisherId, recordId), userId);
    }

    @DeleteMapping("/dislikers/{username}")
    public void removeDislike(@PathVariable String publisher,
                              @PathVariable int recordId,
                              @PathVariable String username) {
        long publisherId = userService.getUserId(publisher);
        long userId = userService.getUserId(username);
        reactionService.removeDislike(new RecordId(publisherId, recordId), userId);
    }
}