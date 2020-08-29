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
    public ResponseEntity<MultipartFile> downloadImg(@PathVariable String publisherId,
                                                     @PathVariable int imgId) {
        throw new NotImplementedException();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadImg(@PathVariable String publisherId) {
        throw new NotImplementedException();
    }

    @PutMapping(path = "/{imgId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateImg(@PathVariable String publisherId,
                          @PathVariable int imgId) {
        throw new NotImplementedException();
    }

    @DeleteMapping(path = "/{imgId}")
    public void removeImg(@PathVariable String publisherId,
                          @PathVariable int imgId) {
        throw new NotImplementedException();
    }
}
