package edu.ukma.blog.controllers.actors;

import edu.ukma.blog.models.Page;
import edu.ukma.blog.models.user.RequestUserDataUpdate;
import edu.ukma.blog.models.user.RequestUserSignup;
import edu.ukma.blog.models.user.ResponseUser;
import edu.ukma.blog.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
@RequestMapping("/users")
public class UserCtrl {

    @Autowired
    private IUserService userService;

    @PostMapping
    public void addUser(@RequestBody RequestUserSignup user) {
        userService.addUser(user);
    }

    // use to load user's page and to get old user data while editing user's profile
    @GetMapping("/{username}")
    public ResponseUser getUserData(@PathVariable String username) {
        return userService.getUser(username);
    }

    @PutMapping("/{username}")
    public void updateUserData(@PathVariable String username,
                               @RequestBody RequestUserDataUpdate update) {
        throw new NotImplementedException();
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

    @DeleteMapping("/{username}")
    public boolean banUser(@PathVariable String username) {
        return userService.banUser(username);
    }
}
