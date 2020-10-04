package edu.ukma.blog.controllers.communication;

import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
@RequestMapping("/users{publisherId}/{userId}/records/{recordId}/likes")
public class LikeCtrl {
    @PostMapping
    public void likeRecord(@PathVariable String publisherId,
                           @PathVariable int recordId,
                           @PathVariable String userId) {
        throw new NotImplementedException();
    }

    @DeleteMapping
    public void removeLike(@PathVariable String publisherId,
                           @PathVariable int recordId,
                           @PathVariable String userId) {
        throw new NotImplementedException();
    }
}
