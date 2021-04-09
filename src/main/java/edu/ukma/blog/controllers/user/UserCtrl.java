package edu.ukma.blog.controllers.user;

import edu.ukma.blog.models.user.requests.EditUserPasswordRequest;
import edu.ukma.blog.models.user.requests.EditUserRequest;
import edu.ukma.blog.models.user.requests.UserSignupRequest;
import edu.ukma.blog.models.user.responses.SignupResponse;
import edu.ukma.blog.models.user.responses.UserDataPreviewResponse;
import edu.ukma.blog.models.user.responses.UserDataResponse;
import edu.ukma.blog.services.interfaces.user_related.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserCtrl {
    @Value("${recordsPreviewBlock}")
    private final int RECORDS_PREVIEW_BLOCK_SIZE;
    private final IUserService userService;

    @PostMapping
    public void addUser(@Valid @RequestBody UserSignupRequest user) {
        userService.createSignUpRequest(user);
    }

    @PostMapping("/confirm/{token}")
    public SignupResponse confirmSignup(@PathVariable String token) {
        return userService.confirmRequest(UUID.fromString(token));
    }

    @GetMapping("/{publisher}")
    public UserDataResponse getUserData(@PathVariable @NotEmpty String publisher,
                                        Principal principal) {
        userService.assertActive(publisher);
        return userService.getPublisher(principal.getName(), publisher);
    }

    @GetMapping("/{publisher}/short")
    public UserDataPreviewResponse getShortData(@PathVariable @NotEmpty String publisher,
                                                Principal principal) {
        userService.assertActive(publisher);
        return userService.getPublisherPreview(publisher, principal.getName(), RECORDS_PREVIEW_BLOCK_SIZE);
    }

    @PatchMapping("/details")
    public void updateUserData(@Valid @RequestBody EditUserRequest update,
                               Principal principal) {
        if (update.getStatus() != null || update.getDescription() != null)
            userService.updateUser(principal.getName(), update);
    }

    @PatchMapping(path = "/password")
    public void updateUserPassword(@Valid @RequestBody EditUserPasswordRequest request, Principal principal) {
        userService.updateUserPassword(principal.getName(), request);
    }

    @DeleteMapping
    public void disableAccount(Principal principal) {
        userService.banUser(principal.getName());
    }
}
