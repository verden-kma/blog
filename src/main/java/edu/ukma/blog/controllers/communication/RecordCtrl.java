package edu.ukma.blog.controllers.communication;

import edu.ukma.blog.constants.ImageConstants;
import edu.ukma.blog.exceptions.server_internal.ServerCriticalError;
import edu.ukma.blog.models.compositeIDs.RecordID;
import edu.ukma.blog.models.record.EditRequestRecord;
import edu.ukma.blog.models.record.RequestRecord;
import edu.ukma.blog.models.record.ResponseRecord;
import edu.ukma.blog.services.IRecordService;
import edu.ukma.blog.services.IRecordImageService;
import edu.ukma.blog.services.IUserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
        recordData.setImage(image);
        long publisherId = userService.getUserId(publisher);
        return recordService.addRecord(publisherId, recordData);
    }

    @GetMapping(path = "/{recordId}")
    public ResponseRecord getRecord(@PathVariable String publisher,
                                    @PathVariable int recordId) {
        long publisherId = userService.getUserId(publisher);
        return recordService.getRecordCore(new RecordID(publisherId, recordId));
    }

    private byte[] getSelectedImage(Function<String, File> selector, String publisher, int recordId) {
        long publisherId = userService.getUserId(publisher);
        String location = recordService.getImgLocation(new RecordID(publisherId, recordId));
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
                           @RequestBody EditRequestRecord updatedRecord) {
        long publisherId = userService.getUserId(publisher);
        recordService.editRecord(new RecordID(publisherId, recordId), updatedRecord);
    }

    @DeleteMapping(path = "/{recordId}")
    public void removeRecord(@PathVariable String publisher,
                             @PathVariable int recordId) {
        long publisherId = userService.getUserId(publisher);
        recordService.removeRecord(new RecordID(publisherId, recordId));
    }
}
