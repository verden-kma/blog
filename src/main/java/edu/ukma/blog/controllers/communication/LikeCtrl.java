package edu.ukma.blog.controllers.communication;

import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
@RequestMapping("/users{publisherId}/records/{recordId}/likes")
public class LikeCtrl {
    @PostMapping("/{userId}")
    public void likeRecord(@PathVariable String publisherId,
                           @PathVariable int recordId,
                           @PathVariable String userId) {
        throw new NotImplementedException();
    }

    @GetMapping
    public int getLikes(@PathVariable String publisherId, @PathVariable int recordId) {
        throw new NotImplementedException();
    }

    @DeleteMapping("/{userId}")
    public void removeLike(@PathVariable String publisherId,
                           @PathVariable int recordId,
                           @PathVariable String userId) {
        throw new NotImplementedException();
    }
}
