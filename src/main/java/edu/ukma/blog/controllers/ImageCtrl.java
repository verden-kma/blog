package edu.ukma.blog.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
@RequestMapping("/users/{publisherId}/images")
public class ImageCtrl {
    @GetMapping(path = "/{imgId}")
    public ResponseEntity<MultipartFile> downloadImg(@PathVariable long publisherId,
                                                     @PathVariable int imgId) {
        throw new NotImplementedException();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadImg(@PathVariable long publisherId) {
        throw new NotImplementedException();
    }

    @PutMapping(path = "/{imgId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateImg(@PathVariable long publisherId,
                          @PathVariable int imgId) {
        throw new NotImplementedException();
    }

    @DeleteMapping(path = "/{imgId}")
    public void removeImg(@PathVariable long publisherId,
                          @PathVariable int imgId) {
        throw new NotImplementedException();
    }
}
