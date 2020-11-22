package edu.ukma.blog.controllers.actors;

import edu.ukma.blog.models.user.requests.EditUserRequestModel;
import edu.ukma.blog.models.user.requests.UserSignupRequest;
import edu.ukma.blog.models.user.responses.PublisherPreview;
import edu.ukma.blog.models.user.responses.UserPageResponse;
import edu.ukma.blog.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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

    // todo: use property accessor value
    @GetMapping("/{username}/short")
    public PublisherPreview getShortData(@PathVariable String username,
                                         @RequestParam @Min(1) @Max(10) int recPrevNum) {
        return userService.getUserPreview(username, recPrevNum);
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
