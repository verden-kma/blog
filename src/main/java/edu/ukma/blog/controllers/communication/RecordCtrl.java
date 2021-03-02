package edu.ukma.blog.controllers.communication;

import edu.ukma.blog.constants.ImageConstants;
import edu.ukma.blog.exceptions.record.BlankRecordEditException;
import edu.ukma.blog.exceptions.server_internal.ServerCriticalError;
import edu.ukma.blog.models.composite_id.RecordId;
import edu.ukma.blog.models.record.MinResponseRecord;
import edu.ukma.blog.models.record.RecordEntity_;
import edu.ukma.blog.models.record.RequestRecord;
import edu.ukma.blog.models.record.ResponseRecord;
import edu.ukma.blog.services.IRecordImageService;
import edu.ukma.blog.services.IRecordService;
import edu.ukma.blog.services.IUserService;
import edu.ukma.blog.utils.EagerContentPage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/users/{publisher}/records")
@RequiredArgsConstructor
public class RecordCtrl {
    @Value("${recordsPerPage}")
    private final int RECORD_PAGE_SIZE;
//            ((PropertyAccessor) SpringApplicationContext
//            .getBean(PropertyAccessor.PROPERTY_ACCESSOR_BEAN_NAME)).getPageSize();

    private final IRecordService recordService;

    private final IUserService userService;

    private final IRecordImageService recordImageService;

    @PostMapping
            (
                    consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
            )
    public int addRecord(@RequestPart @Valid RequestRecord recordData,
                         @RequestPart MultipartFile image,
                         Principal principal) {
        long publisherId = userService.getUserIdByUsername(principal.getName());
        return recordService.addRecord(publisherId, recordData, image);
    }

    @GetMapping
    public EagerContentPage<ResponseRecord> getRecordsPage(@PathVariable @NotEmpty String publisher,
                                                           @RequestParam @Min(1) int page,
                                                           Principal principal) {
        long publisherId = userService.getUserIdByUsername(publisher);
        long userId = userService.getUserIdByUsername(principal.getName());
        Pageable pageable = PageRequest.of(page, RECORD_PAGE_SIZE, Sort.by(RecordEntity_.TIMESTAMP).descending());
        return recordService.getRecordsPage(publisherId, userId, pageable);
    }

    @GetMapping(path = "/{recordId}")
    public ResponseRecord getRecord(@PathVariable @NotEmpty String publisher,
                                    @PathVariable @Min(1) int recordId,
                                    Principal principal) {
        long publisherId = userService.getUserIdByUsername(publisher);
        long userId = userService.getUserIdByUsername(principal.getName());
        return recordService.getRecordCore(new RecordId(publisherId, recordId), userId);
    }

    @GetMapping(path = "/short")
    public List<MinResponseRecord> getRecordsMin(@PathVariable String publisher, @RequestParam List<Integer> rids) {
        return recordService.getSelectedMinResponse(publisher, rids);
    }

    // used (for target view)
    @GetMapping(path = "/{recordId}/image", produces = ImageConstants.TARGET_MEDIA_TYPE)
    public byte[] getImage(@PathVariable @NotEmpty String publisher,
                           @PathVariable @Min(1) int recordId) {
        return getSelectedImage(Collections.singletonList(recordImageService::getImage), publisher, recordId);
    }

    private byte[] getSelectedImage(Iterable<Function<String, File>> selectors, String publisher, int recordId) {
        long publisherId = userService.getUserIdByUsername(publisher);
        String location = recordService.getImgLocation(new RecordId(publisherId, recordId));

        File image = null;
        for (Function<String, File> selector : selectors) {
            if (image == null || !image.exists())
                image = selector.apply(location);
        }
        if (image == null) throw new ServerCriticalError("No valid file provider passed.");

        try (InputStream input = new FileInputStream(image)) {
            return IOUtils.toByteArray(input);
        } catch (IOException e) {
            throw new ServerCriticalError(e);
        }
    }

    // used (for general view)
    @GetMapping(path = "/{recordId}/image-min", produces = ImageConstants.TARGET_MEDIA_TYPE)
    public byte[] getImageMin(@PathVariable @NotEmpty String publisher,
                              @PathVariable @Min(1) int recordId) {
        return getSelectedImage(Arrays.asList(recordImageService::getImageMin, recordImageService::getImage),
                publisher, recordId);
    }

    // used (for digest, user preview)
    @GetMapping(path = "/{recordId}/image-icon", produces = ImageConstants.TARGET_MEDIA_TYPE)
    public byte[] getImageIcon(@PathVariable @NotEmpty String publisher,
                               @PathVariable @Min(1) int recordId) {
        return getSelectedImage(Collections.singletonList(recordImageService::getImageIcon), publisher, recordId);
    }

    @PutMapping(path = "/{recordId}")
    public void editRecord(@PathVariable @Min(1) int recordId,
                           @RequestBody @Valid RequestRecord updatedRecord,
                           Principal principal) {
        if (updatedRecord.getCaption() == null && updatedRecord.getAdText() == null)
            throw new BlankRecordEditException("no update data provided");
        long publisherId = userService.getUserIdByUsername(principal.getName());
        recordService.editRecord(new RecordId(publisherId, recordId), updatedRecord);
    }

    @DeleteMapping(path = "/{recordOwnId}")
    public void removeRecord(@PathVariable @Min(1) int recordOwnId,
                             Principal principal) {
        long publisherId = userService.getUserIdByUsername(principal.getName());
        recordService.removeRecord(new RecordId(publisherId, recordOwnId));
    }
}
