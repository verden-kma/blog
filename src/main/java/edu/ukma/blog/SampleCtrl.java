package edu.ukma.blog;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class SampleCtrl {
    @GetMapping(path = "/") // /users/id
    public String getUserPage() {
        return "user's page";
    }

    @GetMapping(path = "/self/id") // /self/id
    public String getOwnedPage(@AuthenticationPrincipal OAuth2User authentication) {
        System.out.println(authentication.getAttributes());
        return authentication.getAttributes().toString();
    }
}
