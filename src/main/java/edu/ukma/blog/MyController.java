package edu.ukma.blog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @GetMapping(path = "/") // /users/id
    public String getUserPage() {
        return "user's page";
    }

    @GetMapping(path = "/self/id") // /self/id
    public String getOwnedPage() {
        return "your page";
    }
}
