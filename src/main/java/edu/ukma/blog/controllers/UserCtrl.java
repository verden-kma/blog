package edu.ukma.blog.controllers;

import edu.ukma.blog.models.Page;
import edu.ukma.blog.models.User;
import edu.ukma.blog.repositories.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
@RequestMapping("/users")
public class UserCtrl {

    @Autowired
    private UsersRepo usersRepo;

    @PostMapping
    public String addUser(@AuthenticationPrincipal OAuth2User authentication/*, UsersRepo usersRepo*/) {
        if (usersRepo.findById(authentication.getAttribute("sub").toString()).isPresent()) {
            throw new IllegalStateException("such user is already registered");
        }
        User newUser = new User(authentication.getAttribute("sub").toString(), authentication.getAttribute("name").toString());
        usersRepo.save(newUser);
        System.out.println(newUser);
        return newUser.getId();
    }

    // use to load user's page and to get old user data while editing user's profile
    @GetMapping("/{userId}")
    public User getUserData(@PathVariable String userId) {
        throw new NotImplementedException();
    }

    /**
     * @param userId  id of a user whose page is needed
     * @param pageNum number of a page in question
     * @return data about which images should be fetched from server and textual data for {@code pageNum} page
     */
    @GetMapping(path = "/{userId}/{pageNum}")
    public Page getUserPage(@PathVariable String userId,
                            @PathVariable int pageNum) {
        throw new NotImplementedException();
    }

    @DeleteMapping("/{userId}")
    public void banUser(@PathVariable String userId) {
        throw new NotImplementedException();
    }
}
