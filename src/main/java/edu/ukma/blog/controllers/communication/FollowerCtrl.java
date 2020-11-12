package edu.ukma.blog.controllers.communication;

import edu.ukma.blog.PropertyAccessor;
import edu.ukma.blog.SpringApplicationContext;
import edu.ukma.blog.services.IFollowerService;
import edu.ukma.blog.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{publisher}/followers")
public class FollowerCtrl {
    private static final int FOLLOWERS_BLOCK_SIZE;

    static {
        String beanName = PropertyAccessor.class.getSimpleName();
        String propertyAccessorBeanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
        FOLLOWERS_BLOCK_SIZE = ((PropertyAccessor) SpringApplicationContext
                .getBean(propertyAccessorBeanName)).getFollowersBlockSize();

    }

    @Autowired
    private IFollowerService followerService;
    @Autowired
    private IUserService userService;

    @GetMapping(path = "/all")
    public List<String> getFollowers(@PathVariable String publisher) {
        long publisherId = userService.getUserId(publisher);
        return userService.getUsernames(followerService.getFollowers(publisherId));
    }

    @GetMapping
    public List<String> getFollowers(@PathVariable String publisher,
                                     @RequestParam int block) {
        long publisherId = userService.getUserId(publisher);
        Pageable pageable = PageRequest.of(block, FOLLOWERS_BLOCK_SIZE);
        return userService.getUsernames(followerService.getFollowersBlock(publisherId, pageable));
    }

    @PutMapping(path = "/{subscriber}")
    public void follow(@PathVariable String publisher,
                       @PathVariable String subscriber) {
        long publisherId = userService.getUserId(publisher);
        long subscriberId = userService.getUserId(subscriber);
        followerService.addFollower(publisherId, subscriberId);
    }


    @DeleteMapping(path = "/{subscriber}")
    public void unfollow(@PathVariable String publisher,
                         @PathVariable String subscriber) {
        long publisherId = userService.getUserId(publisher);
        long subscriberId = userService.getUserId(subscriber);
        followerService.removeFollower(publisherId, subscriberId);
    }
}
