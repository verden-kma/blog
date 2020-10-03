package edu.ukma.blog.services;

import edu.ukma.blog.exceptions.ServerError;
import edu.ukma.blog.exceptions.WrongFileFormatException;
import edu.ukma.blog.models.compositeIDs.RecordID;
import edu.ukma.blog.models.record.RequestRecord;
import edu.ukma.blog.models.record.ResponseRecord;

public interface IRecordService {
    int addRecord(String username, RequestRecord record) throws ServerError, WrongFileFormatException;

    ResponseRecord getRecord(RecordID id);


    void editRecord();

    void removeRecord();
}
