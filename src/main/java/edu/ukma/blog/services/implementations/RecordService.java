package edu.ukma.blog.services.implementations;

import edu.ukma.blog.exceptions.NoSuchRecordException;
import edu.ukma.blog.exceptions.ServerError;
import edu.ukma.blog.exceptions.WrongFileFormatException;
import edu.ukma.blog.models.compositeIDs.RecordID;
import edu.ukma.blog.models.record.RecordEntity;
import edu.ukma.blog.models.record.RequestRecord;
import edu.ukma.blog.models.record.ResponseRecord;
import edu.ukma.blog.models.user.UserEntity;
import edu.ukma.blog.repositories.ICommentsRepo;
import edu.ukma.blog.repositories.IRecordsRepo;
import edu.ukma.blog.repositories.IUsersRepo;
import edu.ukma.blog.services.IRecordService;
import edu.ukma.blog.services.IUserImageService;
import org.springframework.beans.BeanUtils;
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
    private ICommentsRepo commentsRepo;

    @Autowired
    private IUserImageService imageService;

    @Override
    public int addRecord(String username, RequestRecord record) throws ServerError, WrongFileFormatException {
        UserEntity publisher = usersRepo.findByUsername(username);
        Optional<RecordEntity> lastRecord = recordsRepo.findTopByIdPublisherIdOrderByIdRecordIdDesc(publisher.getId());
        int recordId = lastRecord.map(value -> value.getId().getRecordId() + 1).orElse(1);

        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setId(new RecordID(publisher.getId(), recordId));
        recordEntity.setCaption(record.getCaption());
        recordEntity.setTimestamp(LocalDateTime.parse(record.getTimestamp(), DateTimeFormatter.ISO_DATE_TIME));
        String imgLocation = imageService.saveImage(record.getImage());
        recordEntity.setImgLocation(imgLocation);
        recordsRepo.save(recordEntity);

        return recordId;
    }

    @Override
    public ResponseRecord getRecord(RecordID id) {
        RecordEntity record = recordsRepo.findById(id).orElseThrow(() -> new NoSuchRecordException(id.getRecordId()));
        ResponseRecord res = new ResponseRecord();
        res.setCaption(record.getCaption());
        res.setTimestamp(record.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME));
        BeanUtils.copyProperties(record, res);
        res.setLikes(record.getLikeUsers().size());
        res.setDislikes(record.getDislikeUsers().size());
        res.setNumOfComments(record.getComments().size());
        return res;
    }

    @Override
    public void editRecord() {

    }

    @Override
    public void removeRecord() {

    }
}
