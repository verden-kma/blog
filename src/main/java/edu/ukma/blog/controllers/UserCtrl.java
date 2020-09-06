package edu.ukma.blog.controllers;

import edu.ukma.blog.models.Page;
import edu.ukma.blog.models.user.RequestUserSignup;
import edu.ukma.blog.models.user.ResponseUser;
import edu.ukma.blog.services.implementations.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
@RequestMapping("/users")
public class UserCtrl {

    @Autowired
    private UserService userService;

    @PostMapping
    public long addUser(@RequestBody RequestUserSignup user) {
        return userService.addUser(user).getId();
    }

    // use to load user's page and to get old user data while editing user's profile
    @GetMapping("/{userId}")
    public ResponseUser getUserData(@PathVariable String userId) {
        return new ResponseUser();
//        return userService.getUser(userId);
    }

    /**
     * @param userId  id of a user whose page is needed
     * @param pageNum number of a page in question
     * @return data about which images should be fetched from server and textual data for {@code pageNum} page
     */
    @GetMapping(path = "/{userId}/{pageNum}")
    public Page getUserPage(@PathVariable long userId,
                            @PathVariable int pageNum) {
        throw new NotImplementedException();
    }

    @DeleteMapping("/{userId}")
    public boolean banUser(@PathVariable long userId) {
        return userService.banUser(userId);
    }
}
