package edu.ukma.blog.controllers.actors;

import edu.ukma.blog.PropertyAccessor;
import edu.ukma.blog.SpringApplicationContext;
import edu.ukma.blog.models.user.requests.EditUserRequest;
import edu.ukma.blog.models.user.requests.UserSignupRequest;
import edu.ukma.blog.models.user.responses.UserDataPreviewResponse;
import edu.ukma.blog.models.user.responses.UserDataResponse;
import edu.ukma.blog.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UserCtrl {
    private static final int RECORDS_PREVIEW_BLOCK_SIZE = ((PropertyAccessor) SpringApplicationContext
            .getBean(PropertyAccessor.PROPERTY_ACCESSOR_BEAN_NAME)).getRecordsPreviewBlock();

    private final IUserService userService;

    @Autowired
    public UserCtrl(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void addUser(@Valid @RequestBody UserSignupRequest user) {
        userService.addUser(user);
    }

    // use to load user's page and to get old user data while editing user's profile
    @GetMapping("/{publisher}")
    public UserDataResponse getUserData(@PathVariable @NotEmpty String publisher,
                                        Principal principal) {
        return userService.getPublisher(principal.getName(), publisher);
    }

    // will it be really used?
    @GetMapping("/{publisher}/short")
    public UserDataPreviewResponse getShortData(@PathVariable @NotEmpty String publisher,
                                                Principal principal) {
        return userService.getPublisherPreview(publisher, principal.getName(), RECORDS_PREVIEW_BLOCK_SIZE);
    }

    @PutMapping
    public void updateUserData(@Valid @RequestBody EditUserRequest update,
                               Principal principal) {
        userService.updateUser(principal.getName(), update);
    }

    // possible feature: add admins who can actually ban users, for now it is just self deletion
    @DeleteMapping
    public boolean banUser(Principal principal) {
        return userService.banUser(principal.getName());
    }
}
