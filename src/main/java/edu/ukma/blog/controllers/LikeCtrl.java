package edu.ukma.blog.controllers;

import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
@RequestMapping("/users{publisherId}/{userId}/records/{recordId}/likes")
public class LikeCtrl {
    @PostMapping
    public void likeRecord(@PathVariable long publisherId,
                           @PathVariable int recordId,
                           @PathVariable long userId) {
        throw new NotImplementedException();
    }

    @DeleteMapping
    public void removeLike(@PathVariable long publisherId,
                           @PathVariable int recordId,
                           @PathVariable long userId) {
        throw new NotImplementedException();
    }
}
