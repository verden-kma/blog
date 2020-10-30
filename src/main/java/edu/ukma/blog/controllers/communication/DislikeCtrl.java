package edu.ukma.blog.controllers.communication;

import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
@RequestMapping("/users/{publisherId}/records/{recordId}/dislikes")
public class DislikeCtrl {
    @PostMapping("/{userId}")
    public void dislikeRecord(@PathVariable String publisherId,
                              @PathVariable int recordId,
                              @PathVariable String userId) {
        throw new NotImplementedException();
    }

    @GetMapping
    public int getDislikes(@PathVariable String publisherId, @PathVariable int recordId) {
        throw new NotImplementedException();
    }

    @DeleteMapping("/{userId}")
    public void removeDislike(@PathVariable String publisherId,
                              @PathVariable int recordId,
                              @PathVariable String userId) {
        throw new NotImplementedException();
    }
}
