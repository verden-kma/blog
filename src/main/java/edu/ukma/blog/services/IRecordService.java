package edu.ukma.blog.services;

import edu.ukma.blog.exceptions.server_internal.ServerCriticalError;
import edu.ukma.blog.exceptions.server_internal.WrongFileFormatException;
import edu.ukma.blog.models.compositeIDs.RecordID;
import edu.ukma.blog.models.record.EditRequestRecord;
import edu.ukma.blog.models.record.RequestRecord;
import edu.ukma.blog.models.record.ResponseRecord;

public interface IRecordService {
    int addRecord(String username, RequestRecord record) throws ServerCriticalError, WrongFileFormatException;

    ResponseRecord getRecord(RecordID id);

    void editRecord(RecordID id, EditRequestRecord editRequest);

    void removeRecord(RecordID id);
}
