package edu.ukma.blog.controllers;

import edu.ukma.blog.exceptions.UsernameDuplicateException;
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
    public boolean addUser(@RequestBody RequestUserSignup user) {
        try {
            userService.addUser(user);
        } catch (UsernameDuplicateException e) {
            return false;
        }
        return true;
    }

    // use to load user's page and to get old user data while editing user's profile
    @GetMapping("/{userId}")
    public ResponseUser getUserData(@PathVariable String userId) {
        return userService.getUser(userId);
    }

    /**
     * @param username id of a user whose page is needed
     * @param pageNum  number of a page in question
     * @return data about which images should be fetched from server and textual data for {@code pageNum} page
     */
    @GetMapping(path = "/{username}/{pageNum}")
    public Page getUserPage(@PathVariable String username,
                            @PathVariable int pageNum) {
        throw new NotImplementedException();
    }

    @DeleteMapping("/{userId}")
    public boolean banUser(@PathVariable String userId) {
        return userService.banUser(userId);
    }
}
