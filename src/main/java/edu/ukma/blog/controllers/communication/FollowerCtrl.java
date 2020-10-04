package edu.ukma.blog.controllers.communication;

import edu.ukma.blog.models.user.UserEntity;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/followers")
public class FollowerCtrl {
    @GetMapping
    public List<UserEntity> getFollowers(@PathVariable String userId) {
        throw new NotImplementedException();
    }

    @PutMapping(path = "/{followerId}")
    public void follow(@PathVariable String userId,
                       @PathVariable String followerId) {
        throw new NotImplementedException();
    }

    @DeleteMapping(path = "/{followerId}")
    public void unfollow(@PathVariable String userId,
                         @PathVariable String followerId) {
        throw new NotImplementedException();
    }
}
