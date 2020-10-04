package edu.ukma.blog.controllers.communication;

import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
@RequestMapping("/users{publisherId}/{userId}/records/{recordId}/dislikes")
public class DislikeCtrl {
    @PostMapping
    public void dislikeRecord(@PathVariable String publisherId,
                              @PathVariable int recordId,
                              @PathVariable String userId) {
        throw new NotImplementedException();
    }

    @DeleteMapping
    public void removeDislike(@PathVariable String publisherId,
                              @PathVariable int recordId,
                              @PathVariable String userId) {
        throw new NotImplementedException();
    }
}
