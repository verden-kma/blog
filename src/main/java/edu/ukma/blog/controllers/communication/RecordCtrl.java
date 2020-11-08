package edu.ukma.blog.controllers.communication;

import edu.ukma.blog.constants.ImageConstants;
import edu.ukma.blog.exceptions.record.BlankRecordEditException;
import edu.ukma.blog.exceptions.server_internal.ServerCriticalError;
import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.models.record.RequestRecord;
import edu.ukma.blog.models.record.ResponseRecord;
import edu.ukma.blog.services.IRecordImageService;
import edu.ukma.blog.services.IRecordService;
import edu.ukma.blog.services.IUserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

@RestController
@RequestMapping("/users/{publisher}/records")
public class RecordCtrl {

    @Autowired
    private IRecordService recordService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRecordImageService recordImageService;

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public int addRecord(@PathVariable String publisher,
                         @RequestPart RequestRecord recordData,
                         @RequestPart MultipartFile image) {
        long publisherId = userService.getUserId(publisher);
        return recordService.addRecord(publisherId, recordData, image);
    }

    @GetMapping(path = "/{recordId}/{username}")
    public ResponseRecord getRecord(@PathVariable String publisher,
                                    @PathVariable int recordId,
                                    @PathVariable String username) {
        long publisherId = userService.getUserId(publisher);
        long userId = userService.getUserId(username);
        return recordService.getRecordCore(new RecordId(publisherId, recordId), userId);
    }

    private byte[] getSelectedImage(Function<String, File> selector, String publisher, int recordId) {
        long publisherId = userService.getUserId(publisher);
        String location = recordService.getImgLocation(new RecordId(publisherId, recordId));
        File image = selector.apply(location);
        try (InputStream input = new FileInputStream(image)) {
            return IOUtils.toByteArray(input);
        } catch (IOException e) {
            throw new ServerCriticalError(e);
        }
    }

    @GetMapping(path = "/{recordId}/image", produces = ImageConstants.TARGET_MEDIA_TYPE)
    public byte[] getImage(@PathVariable String publisher,
                           @PathVariable int recordId) {
        return getSelectedImage(recordImageService::getImage, publisher, recordId);
    }

    @GetMapping(path = "/{recordId}/image-min", produces = ImageConstants.TARGET_MEDIA_TYPE)
    public byte[] getImageMin(@PathVariable String publisher,
                              @PathVariable int recordId) {
        return getSelectedImage(recordImageService::getImageMin, publisher, recordId);
    }

    @GetMapping(path = "/{recordId}/image-icon", produces = ImageConstants.TARGET_MEDIA_TYPE)
    public byte[] getImageIcon(@PathVariable String publisher,
                               @PathVariable int recordId) {
        return getSelectedImage(recordImageService::getImageIcon, publisher, recordId);
    }

    @PutMapping(path = "/{recordId}")
    public void editRecord(@PathVariable String publisher,
                           @PathVariable int recordId,
                           @RequestBody RequestRecord updatedRecord) {
        if (updatedRecord.getCaption() == null && updatedRecord.getAdText() == null)
            throw new BlankRecordEditException("no update data provided");
        long publisherId = userService.getUserId(publisher);
        recordService.editRecord(new RecordId(publisherId, recordId), updatedRecord);
    }

    @DeleteMapping(path = "/{recordId}")
    public void removeRecord(@PathVariable String publisher,
                             @PathVariable int recordId) {
        long publisherId = userService.getUserId(publisher);
        recordService.removeRecord(new RecordId(publisherId, recordId));
    }
}
