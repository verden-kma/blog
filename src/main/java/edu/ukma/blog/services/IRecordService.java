package edu.ukma.blog.services;

import edu.ukma.blog.models.RequestRecord;

public interface IRecordService {
    int addRecord(String username, RequestRecord record);

    void editRecord();

    void removeRecord();
}
