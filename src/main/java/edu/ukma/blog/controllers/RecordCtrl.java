package edu.ukma.blog.controllers;

import edu.ukma.blog.models.RequestRecord;
import edu.ukma.blog.services.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
@RequestMapping("/users/{publisher}/records")
public class RecordCtrl {
    @Autowired
    private IRecordService recordService;

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public int addRecord(@PathVariable String publisher,
                         @RequestPart RequestRecord recordData,
                         @RequestPart MultipartFile image) {
        recordData.setImage(image);
        return recordService.addRecord(publisher, recordData);
    }

    @PutMapping(path = "/{recordId}")
    public void editRecord(@PathVariable long publisher,
                           @PathVariable int recordId,
                           @RequestBody RequestRecord updatedRecord) {
        throw new NotImplementedException();
    }

    @DeleteMapping(path = "/{recordId}")
    public void removeRecord(@PathVariable long publisher,
                             @PathVariable int recordId) {

    }
}
