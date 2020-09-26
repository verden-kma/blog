package edu.ukma.blog.services;

import edu.ukma.blog.exceptions.ServerError;
import edu.ukma.blog.exceptions.WrongFileFormatException;
import edu.ukma.blog.models.RequestRecord;

public interface IRecordService {
    int addRecord(String username, RequestRecord record) throws ServerError, WrongFileFormatException;

    void editRecord();

    void removeRecord();
}
