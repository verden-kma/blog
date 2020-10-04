package edu.ukma.blog.controllers;

import edu.ukma.blog.models.Page;
import edu.ukma.blog.models.user.RequestUserDataUpdate;
import edu.ukma.blog.models.user.RequestUserSignup;
import edu.ukma.blog.models.user.ResponseUser;
import edu.ukma.blog.services.implementations.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;

@RestController
@RequestMapping("/users")
public class UserCtrl {

    @Autowired
    private UserService userService;

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

    @PutMapping(value = "/{username}/avatar", consumes = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public void setAvatarImage(@PathVariable String username,
                               @RequestBody MultipartFile icon) {
        throw new NotImplementedException();
    }

    @GetMapping(value = "/{username}/avatar", produces = {MediaType.IMAGE_JPEG_VALUE})
    public byte[] getAvatarImage(@PathVariable String username) {
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

    @DeleteMapping("/{userId}")
    public boolean banUser(@PathVariable String userId) {
        return userService.banUser(userId);
    }
}
