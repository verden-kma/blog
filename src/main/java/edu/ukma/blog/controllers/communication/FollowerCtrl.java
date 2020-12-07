package edu.ukma.blog.controllers.communication;

import edu.ukma.blog.PropertyAccessor;
import edu.ukma.blog.SpringApplicationContext;
import edu.ukma.blog.services.IFollowerService;
import edu.ukma.blog.services.IUserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users/{publisher}/followers")
public class FollowerCtrl {
    private static final int FOLLOWERS_BLOCK_SIZE = ((PropertyAccessor) SpringApplicationContext
            .getBean(PropertyAccessor.PROPERTY_ACCESSOR_BEAN_NAME)).getFollowersBlockSize();

    private final IFollowerService followerService;

    private final IUserService userService;

    public FollowerCtrl(IFollowerService followerService, IUserService userService) {
        this.followerService = followerService;
        this.userService = userService;
    }

    @GetMapping
    public List<String> getFollowers(@PathVariable String publisher,
                                     @RequestParam int block) {
        long publisherId = userService.getUserIdByUsername(publisher);
        Pageable pageable = PageRequest.of(block, FOLLOWERS_BLOCK_SIZE);
        return userService.getUsernamesByIds(followerService.getFollowersBlock(publisherId, pageable));
    }

    // used (in target view, in publisher page view, publisher preview)
    @PutMapping
    public void follow(@PathVariable String publisher,
                       Principal principal) {
        long publisherId = userService.getUserIdByUsername(publisher);
        long subscriberId = userService.getUserIdByUsername(principal.getName());
        followerService.addFollower(publisherId, subscriberId);
    }

    // used (same)
    @DeleteMapping
    public void unfollow(@PathVariable String publisher,
                         Principal principal) {
        long publisherId = userService.getUserIdByUsername(publisher);
        long subscriberId = userService.getUserIdByUsername(principal.getName());
        followerService.removeFollower(publisherId, subscriberId);
    }
}
