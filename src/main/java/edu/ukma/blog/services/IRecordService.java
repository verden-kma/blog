package edu.ukma.blog.services;

import edu.ukma.blog.exceptions.server_internal.ServerCriticalError;
import edu.ukma.blog.exceptions.server_internal.WrongFileFormatException;
import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.models.record.MinResponseRecord;
import edu.ukma.blog.models.record.RequestRecord;
import edu.ukma.blog.models.record.ResponseRecord;
import edu.ukma.blog.utils.EagerContentPage;
import edu.ukma.blog.utils.LazyContentPage;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// works with textual data only
public interface IRecordService {
    int addRecord(long userId, RequestRecord record, MultipartFile image)
            throws ServerCriticalError, WrongFileFormatException;

    EagerContentPage<ResponseRecord> getRecordsPage(long publisherId, long userId, Pageable pageable);

    List<String> getUserRecordsImgPaths(long userId, Pageable pageable);

    LazyContentPage<MinResponseRecord> getMinResponsePage(Pageable pageable);

    ResponseRecord getRecordCore(RecordId id, long userId);

    String getImgLocation(RecordId id);

    void editRecord(RecordId id, RequestRecord editRequest);

    void removeRecord(RecordId id);
}
