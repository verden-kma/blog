package edu.ukma.blog.services;

import edu.ukma.blog.exceptions.server_internal.ServerCriticalError;
import edu.ukma.blog.exceptions.server_internal.WrongFileFormatException;
import edu.ukma.blog.models.compositeIDs.RecordID;
import edu.ukma.blog.models.record.RequestRecord;
import edu.ukma.blog.models.record.ResponseRecord;
import org.springframework.web.multipart.MultipartFile;

// works with textual data only
public interface IRecordService {
    int addRecord(long userId, RequestRecord record, MultipartFile image)
            throws ServerCriticalError, WrongFileFormatException;

    ResponseRecord getRecordCore(RecordID id);

    String getImgLocation(RecordID id);

    void editRecord(RecordID id, RequestRecord editRequest);

    void removeRecord(RecordID id);
}
