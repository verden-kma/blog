package edu.ukma.blog.controllers.user;

import edu.ukma.blog.models.composite_id.CommentId;
import edu.ukma.blog.models.composite_id.RecordId;
import edu.ukma.blog.models.user.requests.UserSignupRequest;
import edu.ukma.blog.services.interfaces.record_related.ICommentService;
import edu.ukma.blog.services.interfaces.record_related.IRecordService;
import edu.ukma.blog.services.interfaces.user_related.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminCtrl {
    private final IUserService userService;
    private final IRecordService recordService;
    private final ICommentService commentService;

    @PostMapping
    @PreAuthorize("hasAuthority(ADD_ADMINS)")
    public void addAdmin(@RequestBody UserSignupRequest signupRequest) {
        userService.createAdmin(signupRequest);
    }

    @DeleteMapping("/record/{publisher}/{recordId}")
    @PreAuthorize("hasAuthority(DELETE_OTHER_RECORD)")
    public void removeRecord(@PathVariable String publisher, @PathVariable int recordId) {
        long publisherId = userService.getUserIdByUsername(publisher);
        recordService.removeRecord(new RecordId(publisherId, recordId));
    }

    @DeleteMapping("/comment/{publisher}/{recordId}/{commentId}")
    @PreAuthorize("hasAuthority(DELETE_OTHER_COMMENT)")
    public void removeComment(@PathVariable String publisher, @PathVariable int recordId, @PathVariable int commentId) {
        long publisherId = userService.getUserIdByUsername(publisher);
        commentService.removeComment(new CommentId(new RecordId(publisherId, recordId), commentId), publisherId);
    }

    @PatchMapping("/user-ban/{publisher}")
    @PreAuthorize("hasAuthority(BAN_OTHERS)")
    public void banUser(@PathVariable String publisher) {
        userService.banUser(publisher);
    }

    @DeleteMapping("/user-ban/{publisher}")
    @PreAuthorize("hasAuthority(BAN_OTHERS)")
    public void cancelBan(@PathVariable String publisher) {
        userService.cancelBan(publisher);
    }
}
