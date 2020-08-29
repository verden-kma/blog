package edu.ukma.blog;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class SampleCtrl {
//    @GetMapping(path = "/") // /users/id
//    public String getUserPage() {
//        return "user's page";
//    }

//    @GetMapping(path = "/self/id") // /self/id
//    public String getOwnedPage(@AuthenticationPrincipal OAuth2User authentication) {
//        System.out.println("id = " + authentication.getAttribute("sub"));
//        System.out.println("username = " + authentication.getAttribute("name"));
//        return authentication.getAttributes().toString();
//    }
}
