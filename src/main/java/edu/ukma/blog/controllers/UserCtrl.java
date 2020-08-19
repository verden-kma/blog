package edu.ukma.blog.controllers;

import edu.ukma.blog.models.Page;
import edu.ukma.blog.models.User;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
@RequestMapping("/users")
public class UserCtrl {
    // use to load user's page and to get old user data while editing user's profile
    @GetMapping("/{userId}")
    public User getUserData(@PathVariable long userId) {
        throw new NotImplementedException();
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
    public void banUser(@PathVariable long userId) {
        throw new NotImplementedException();
    }
}
