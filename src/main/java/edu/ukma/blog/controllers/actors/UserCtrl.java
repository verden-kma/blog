package edu.ukma.blog.controllers.actors;

import edu.ukma.blog.models.user.requests.EditUserRequestModel;
import edu.ukma.blog.models.user.requests.UserSignupRequest;
import edu.ukma.blog.models.user.responses.UserPageResponse;
import edu.ukma.blog.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserCtrl {

    @Autowired
    private IUserService userService;

    @PostMapping
    public void addUser(@RequestBody UserSignupRequest user) {
        userService.addUser(user);
    }

    // use to load user's page and to get old user data while editing user's profile
    @GetMapping("/{username}")
    public UserPageResponse getUserData(@PathVariable String username) {
        return userService.getUser(username);
    }

    @PutMapping("/{username}")
    public void updateUserData(@PathVariable String username,
                               @Valid @RequestBody EditUserRequestModel update) {
        userService.updateUser(username, update);
    }

    @DeleteMapping("/{username}")
    public boolean banUser(@PathVariable String username) {
        return userService.banUser(username);
    }
}
