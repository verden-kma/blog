package edu.ukma.blog.controllers.communication;

import edu.ukma.blog.PropertyAccessor;
import edu.ukma.blog.SpringApplicationContext;
import edu.ukma.blog.constants.ImageConstants;
import edu.ukma.blog.exceptions.record.BlankRecordEditException;
import edu.ukma.blog.exceptions.server_internal.ServerCriticalError;
import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.models.record.RecordEntity_;
import edu.ukma.blog.models.record.RequestRecord;
import edu.ukma.blog.models.record.ResponseRecord;
import edu.ukma.blog.services.IRecordImageService;
import edu.ukma.blog.services.IRecordService;
import edu.ukma.blog.services.IUserService;
import edu.ukma.blog.utils.EagerContentPage;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.function.Function;

@RestController
@RequestMapping("/users/{publisher}/records")
public class RecordCtrl {
    private static final int RECORD_PAGE_SIZE = ((PropertyAccessor) SpringApplicationContext
            .getBean(PropertyAccessor.PROPERTY_ACCESSOR_BEAN_NAME)).getPageSize();

    private final IRecordService recordService;

    private final IUserService userService;

    private final IRecordImageService recordImageService;

    public RecordCtrl(IRecordService recordService, IUserService userService, IRecordImageService recordImageService) {
        this.recordService = recordService;
        this.userService = userService;
        this.recordImageService = recordImageService;
    }

    @GetMapping
    public EagerContentPage<ResponseRecord> getRecordsPage(@PathVariable String publisher,
                                                           @RequestParam int page,
                                                           Principal principal) {
        long publisherId = userService.getUserId(publisher);
        long userId = userService.getUserId(principal.getName());
        Pageable pageable = PageRequest.of(page, RECORD_PAGE_SIZE, Sort.by(RecordEntity_.TIMESTAMP).descending());
        return recordService.getRecordsPage(publisherId, userId, pageable);
    }

    // todo: handle validation in controller to avoid 500 error while persisting
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public int addRecord(@RequestPart RequestRecord recordData,
                         @RequestPart MultipartFile image,
                         Principal principal) {
        long publisherId = userService.getUserId(principal.getName());
        return recordService.addRecord(publisherId, recordData, image);
    }

    @GetMapping(path = "/{recordId}")
    public ResponseRecord getRecord(@PathVariable String publisher,
                                    @PathVariable int recordId,
                                    Principal principal) {
        long publisherId = userService.getUserId(publisher);
        long userId = userService.getUserId(principal.getName());
        return recordService.getRecordCore(new RecordId(publisherId, recordId), userId);
    }

    // used (for target view)
    @GetMapping(path = "/{recordId}/image", produces = ImageConstants.TARGET_MEDIA_TYPE)
    public byte[] getImage(@PathVariable String publisher,
                           @PathVariable int recordId) {
        return getSelectedImage(recordImageService::getImage, publisher, recordId);
    }

    // used (for general view)
    @GetMapping(path = "/{recordId}/image-min", produces = ImageConstants.TARGET_MEDIA_TYPE)
    public byte[] getImageMin(@PathVariable String publisher,
                              @PathVariable int recordId) {
        return getSelectedImage(recordImageService::getImageMin, publisher, recordId);
    }

    // used (for digest, user preview)
    @GetMapping(path = "/{recordId}/image-icon", produces = ImageConstants.TARGET_MEDIA_TYPE)
    public byte[] getImageIcon(@PathVariable String publisher,
                               @PathVariable int recordId) {
        return getSelectedImage(recordImageService::getImageIcon, publisher, recordId);
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

    @PutMapping(path = "/{recordId}")
    public void editRecord(@PathVariable int recordId,
                           @RequestBody RequestRecord updatedRecord,
                           Principal principal) {
        if (updatedRecord.getCaption() == null && updatedRecord.getAdText() == null)
            throw new BlankRecordEditException("no update data provided");
        long publisherId = userService.getUserId(principal.getName());
        recordService.editRecord(new RecordId(publisherId, recordId), updatedRecord);
    }

    @DeleteMapping(path = "/{recordOwnId}")
    public void removeRecord(@PathVariable int recordOwnId,
                             Principal principal) {
        long publisherId = userService.getUserId(principal.getName());
        recordService.removeRecord(new RecordId(publisherId, recordOwnId));
    }
}
