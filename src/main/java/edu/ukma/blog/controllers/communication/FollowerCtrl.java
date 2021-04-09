package edu.ukma.blog.controllers.communication;

import edu.ukma.blog.exceptions.user.SelfFollowerException;
import edu.ukma.blog.services.interfaces.user_related.IFollowerService;
import edu.ukma.blog.services.interfaces.user_related.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users/{publisher}/followers")
@RequiredArgsConstructor
public class FollowerCtrl {
    @Value("${followersPerBlock}")
    private final int FOLLOWERS_BLOCK_SIZE;

    private final IFollowerService followerService;

    private final IUserService userService;

    @GetMapping
    public List<String> getFollowers(@PathVariable @NotEmpty String publisher,
                                     @RequestParam int block) {
        userService.assertActive(publisher);
        long publisherId = userService.getUserIdByUsername(publisher);
        Pageable pageable = PageRequest.of(block, FOLLOWERS_BLOCK_SIZE);
        return userService.getUsernamesByIds(followerService.getFollowersBlock(publisherId, pageable));
    }

    // used (in target view, in publisher page view, publisher preview)
    @PreAuthorize("hasAuthority('FOLLOW')")
    @PutMapping
    public void follow(@PathVariable @NotEmpty String publisher,
                       Principal principal) {
        userService.assertActive(publisher);
        if (publisher.equals(principal.getName())) throw new SelfFollowerException();
        long publisherId = userService.getUserIdByUsername(publisher);
        long subscriberId = userService.getUserIdByUsername(principal.getName());
        followerService.addFollower(publisherId, subscriberId);
    }

    @PreAuthorize("hasAuthority('FOLLOW')")
    @DeleteMapping
    public void unfollow(@PathVariable @NotEmpty String publisher,
                         Principal principal) {
        long publisherId = userService.getUserIdByUsername(publisher);
        long subscriberId = userService.getUserIdByUsername(principal.getName());
        followerService.removeFollower(publisherId, subscriberId);
    }
}
