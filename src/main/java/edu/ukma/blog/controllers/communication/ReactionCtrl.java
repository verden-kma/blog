package edu.ukma.blog.controllers.communication;

import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.services.IRecordEvalService;
import edu.ukma.blog.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{publisher}/records/{recordId}")
public class ReactionCtrl {
    /*
     * user can load page on 2 devices (tabs) [null]
     * put like on d1, this like will not be displayed on d2 [true]
     * go to d2, put like again, this action should not remove like,
     * therefore there are separate put and delete endpoints
     * */

    @Autowired
    private IRecordEvalService reactionService;

    @Autowired
    private IUserService userService;

    @GetMapping("/likers")
    public List<String> getLikers(@PathVariable String publisher,
                                  @PathVariable int recordId,
                                  @RequestParam int block) {
        long publisherId = userService.getUserId(publisher);
        return reactionService.getLikers(new RecordId(publisherId, recordId));
    }

    @PutMapping("/likes/{username}")
    public void likeRecord(@PathVariable String publisher,
                           @PathVariable int recordId,
                           @PathVariable String username) {
        long publisherId = userService.getUserId(publisher);
        long userId = userService.getUserId(username);
        reactionService.putLike(new RecordId(publisherId, recordId), userId);
    }

    @DeleteMapping("/likes/{username}")
    public void removeLike(@PathVariable String publisher,
                           @PathVariable int recordId,
                           @PathVariable String username) {
        long publisherId = userService.getUserId(publisher);
        long userId = userService.getUserId(username);
        reactionService.removeLike(new RecordId(publisherId, recordId), userId);
    }

    @GetMapping("/dislikers")
    public List<String> getDislikers(@PathVariable String publisher,
                                     @PathVariable int recordId,
                                     @RequestParam int block) {
        long publisherId = userService.getUserId(publisher);
        return reactionService.getDislikers(new RecordId(publisherId, recordId));
    }

    @PutMapping("/dislikes/{username}")
    public void dislikeRecord(@PathVariable String publisher,
                              @PathVariable int recordId,
                              @PathVariable String username) {
        long publisherId = userService.getUserId(publisher);
        long userId = userService.getUserId(username);
        reactionService.putDislike(new RecordId(publisherId, recordId), userId);
    }

    @DeleteMapping("/dislikes/{username}")
    public void removeDislike(@PathVariable String publisher,
                              @PathVariable int recordId,
                              @PathVariable String username) {
        long publisherId = userService.getUserId(publisher);
        long userId = userService.getUserId(username);
        reactionService.removeDislike(new RecordId(publisherId, recordId), userId);
    }
}