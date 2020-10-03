package edu.ukma.blog.controllers;

import edu.ukma.blog.exceptions.UsernameMissingException;
import edu.ukma.blog.models.compositeIDs.RecordID;
import edu.ukma.blog.models.record.RequestRecord;
import edu.ukma.blog.models.record.ResponseRecord;
import edu.ukma.blog.repositories.IUsersRepo;
import edu.ukma.blog.services.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Optional;

@RestController
@RequestMapping("/users/{publisher}/records")
public class RecordCtrl {
    // ResponseEntity<MultipartFile>
    @Autowired
    private IRecordService recordService;

    @Autowired
    private IUsersRepo usersRepo;

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public int addRecord(@PathVariable String publisher,
                         @RequestPart RequestRecord recordData,
                         @RequestPart MultipartFile image) {
        recordData.setImage(image);
        return recordService.addRecord(publisher, recordData);
    }

    @GetMapping(path = "/{recordId}")
    public ResponseRecord getRecord(@PathVariable String publisher,
                                    @PathVariable int recordId) {
        Optional<Long> publisherMaybeId = usersRepo.getIdByUsername(publisher);
        long publisherId = publisherMaybeId.orElseThrow(() -> new UsernameMissingException(publisher));
        return recordService.getRecord(new RecordID(publisherId, recordId));
    }

    @GetMapping(path = "/{recordId}/image")
    public byte[] getImage(@PathVariable String publisher,
                           @PathVariable int recordId) {
        throw new NotImplementedException();
    }

    @GetMapping(path = "/{recordId}/image-min")
    public byte[] getImageMin(@PathVariable String publisher,
                              @PathVariable int recordId) {
        throw new NotImplementedException();
    }

    @PutMapping(path = "/{recordId}")
    public void editRecord(@PathVariable String publisher,
                           @PathVariable int recordId,
                           @RequestBody RequestRecord updatedRecord) {
        throw new NotImplementedException();
    }

    @DeleteMapping(path = "/{recordId}")
    public void removeRecord(@PathVariable String publisher,
                             @PathVariable int recordId) {

    }
}
