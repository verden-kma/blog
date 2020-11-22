package edu.ukma.blog.services;

import edu.ukma.blog.exceptions.server_internal.ServerCriticalError;
import edu.ukma.blog.exceptions.server_internal.WrongFileFormatException;
import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.models.record.RecordsPage;
import edu.ukma.blog.models.record.RequestRecord;
import edu.ukma.blog.models.record.ResponseRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// works with textual data only
public interface IRecordService {
    int addRecord(long userId, RequestRecord record, MultipartFile image)
            throws ServerCriticalError, WrongFileFormatException;

    RecordsPage getRecordsPage(long publisherId, long userId, Pageable pageable);

    List<Integer> getLatestRecordsIds(int n);

    ResponseRecord getRecordCore(RecordId id, long userId);

    String getImgLocation(RecordId id);

    void editRecord(RecordId id, RequestRecord editRequest);

    void removeRecord(RecordId id);
}
