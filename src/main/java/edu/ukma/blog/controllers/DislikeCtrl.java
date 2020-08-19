package edu.ukma.blog.controllers;

import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
@RequestMapping("/users{publisherId}/{userId}/records/{recordId}/dislikes")
public class DislikeCtrl {
    @PostMapping
    public void dislikeRecord(@PathVariable long publisherId,
                              @PathVariable int recordId,
                              @PathVariable long userId) {
        throw new NotImplementedException();
    }

    @DeleteMapping
    public void removeDislike(@PathVariable long publisherId,
                              @PathVariable int recordId,
                              @PathVariable long userId) {
        throw new NotImplementedException();
    }
}
