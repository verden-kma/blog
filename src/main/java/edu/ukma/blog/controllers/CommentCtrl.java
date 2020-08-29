package edu.ukma.blog.controllers;

import edu.ukma.blog.models.Comment;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
@RequestMapping("/users/{publisherId}/records/{recordId}/comments")
public class CommentCtrl {
    /**
     * stores a comment in database, assigns it to publisher/record/nextComment
     *
     * @param publisherId - id of a content creator
     * @param recordId    - id of a record
     * @param newComment  - core comment data
     * @return valid id of a new comment in a database
     */
    @PostMapping
    public int addComment(@PathVariable String publisherId,
                          @PathVariable int recordId,
                          @RequestBody Comment newComment) {
        throw new NotImplementedException();
    }

    @DeleteMapping(path = "/{commentId}")
    public void removeComment(@PathVariable String publisherId,
                              @PathVariable int recordId,
                              @PathVariable int commentId) {
        throw new NotImplementedException();
    }
}
