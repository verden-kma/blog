package edu.ukma.blog.controllers;

import lombok.Data;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/users/{publisherId}/records")
public class RecordCtrl {
    @PostMapping
    public int addRecord(@PathVariable long publisherId,
                         @RequestBody RawRecord recordData) {
        throw new NotImplementedException();
    }

    @PutMapping(path = "/{recordId}")
    public void editRecord(@PathVariable long publisherId,
                           @PathVariable int recordId,
                           @RequestBody RawRecord updatedRecord) {
        throw new NotImplementedException();
    }

    @DeleteMapping(path = "/{recordId}")
    public void removeRecord(@PathVariable long publisherId,
                             @PathVariable int recordId) {

    }
}

@Data
class RawRecord {
    private String caption;

    private MultipartFile image;

    private LocalDateTime timestamp;
}
