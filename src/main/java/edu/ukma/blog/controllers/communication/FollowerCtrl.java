package edu.ukma.blog.controllers.communication;

import edu.ukma.blog.exceptions.user.SelfFollowerException;
import edu.ukma.blog.services.IFollowerService;
import edu.ukma.blog.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
//            = ((PropertyAccessor) SpringApplicationContext
//            .getBean(PropertyAccessor.PROPERTY_ACCESSOR_BEAN_NAME)).getFollowersBlockSize();

    private final IFollowerService followerService;

    private final IUserService userService;

    @GetMapping
    public List<String> getFollowers(@PathVariable @NotEmpty String publisher,
                                     @RequestParam int block) {
        long publisherId = userService.getUserIdByUsername(publisher);
        Pageable pageable = PageRequest.of(block, FOLLOWERS_BLOCK_SIZE);
        return userService.getUsernamesByIds(followerService.getFollowersBlock(publisherId, pageable));
    }

    // used (in target view, in publisher page view, publisher preview)
    @PutMapping
    public void follow(@PathVariable @NotEmpty String publisher,
                       Principal principal) {
        if (publisher.equals(principal.getName())) throw new SelfFollowerException();
        long publisherId = userService.getUserIdByUsername(publisher);
        long subscriberId = userService.getUserIdByUsername(principal.getName());
        followerService.addFollower(publisherId, subscriberId);
    }

    // used (same)
    @DeleteMapping
    public void unfollow(@PathVariable @NotEmpty String publisher,
                         Principal principal) {
        long publisherId = userService.getUserIdByUsername(publisher);
        long subscriberId = userService.getUserIdByUsername(principal.getName());
        followerService.removeFollower(publisherId, subscriberId);
    }
}
