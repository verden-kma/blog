package edu.ukma.blog.controllers;

import edu.ukma.blog.models.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchCtrl {
    @GetMapping
    public List<User> findUsers(@RequestParam String username) {
        throw new NotImplementedException();
    }
}
