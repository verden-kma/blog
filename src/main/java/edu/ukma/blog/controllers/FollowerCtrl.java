package edu.ukma.blog.controllers;

import edu.ukma.blog.models.User;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/followers")
public class FollowerCtrl {
    @GetMapping
    public List<User> getFollowers(@PathVariable long userId) {
        throw new NotImplementedException();
    }

    @PutMapping(path = "/{followerId}")
    public void follow(@PathVariable long userId,
                       @PathVariable long followerId) {
        throw new NotImplementedException();
    }

    @DeleteMapping(path = "/{followerId}")
    public void unfollow(@PathVariable long userId,
                         @PathVariable long followerId) {
        throw new NotImplementedException();
    }
}
