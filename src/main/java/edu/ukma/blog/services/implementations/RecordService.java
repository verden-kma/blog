package edu.ukma.blog.services.implementations;

import edu.ukma.blog.exceptions.ServerError;
import edu.ukma.blog.exceptions.WrongFileFormatException;
import edu.ukma.blog.models.Record;
import edu.ukma.blog.models.RequestRecord;
import edu.ukma.blog.models.compositeIDs.RecordID;
import edu.ukma.blog.models.user.UserEntity;
import edu.ukma.blog.repositories.IRecordsRepo;
import edu.ukma.blog.repositories.IUsersRepo;
import edu.ukma.blog.services.IRecordService;
import edu.ukma.blog.services.IUserImageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class RecordService implements IRecordService {
    @Autowired
    private IRecordsRepo recordsRepo;

    @Autowired
    private IUsersRepo usersRepo;

    @Autowired
    private IUserImageManager imageService;

    @Override
    public int addRecord(String username, RequestRecord record) throws ServerError, WrongFileFormatException {
        UserEntity publisher = usersRepo.findByUsername(username);
        Optional<Record> lastRecord = recordsRepo.findTopByIdPublisherIdOrderByIdRecordIdDesc(publisher.getId());
        int recordId = lastRecord.map(value -> value.getId().getRecordId() + 1).orElse(1);

        Record recordEntity = new Record();
        recordEntity.setId(new RecordID(publisher.getId(), recordId));
        recordEntity.setCaption(record.getCaption());
        recordEntity.setTimestamp(LocalDateTime.parse(record.getTimestamp(), DateTimeFormatter.ISO_DATE_TIME));
        String imgLocation = imageService.saveImage(record.getImage());
        recordEntity.setImgLocation(imgLocation);
        recordsRepo.save(recordEntity);

        return recordId;
    }

    @Override
    public void editRecord() {

    }

    @Override
    public void removeRecord() {

    }
}
