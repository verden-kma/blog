package edu.ukma.blog.services.implementations;

import edu.ukma.blog.exceptions.record.BlankRecordEditException;
import edu.ukma.blog.exceptions.record.NoSuchRecordException;
import edu.ukma.blog.exceptions.server_internal.ServerCriticalError;
import edu.ukma.blog.exceptions.server_internal.ServerLogicsError;
import edu.ukma.blog.exceptions.server_internal.WrongFileFormatException;
import edu.ukma.blog.models.compositeIDs.RecordID;
import edu.ukma.blog.models.record.RecordEntity;
import edu.ukma.blog.models.record.RequestRecord;
import edu.ukma.blog.models.record.ResponseRecord;
import edu.ukma.blog.repositories.ICommentsRepo;
import edu.ukma.blog.repositories.IRecordsRepo;
import edu.ukma.blog.services.IRecordService;
import edu.ukma.blog.services.IRecordImageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Optional;

@Service
public class RecordService implements IRecordService {

    @Autowired
    private IRecordsRepo recordsRepo;

    @Autowired
    private ICommentsRepo commentsRepo;

    @Autowired
    private IRecordImageService imageService;

    @Override
    public int addRecord(long publisherId, RequestRecord record, MultipartFile image)
            throws ServerCriticalError, WrongFileFormatException {
        Optional<RecordEntity> lastRecord = recordsRepo.findTopById_PublisherIdOrderById_RecordIdDesc(publisherId);
        int recordId = lastRecord.map(value -> value.getId().getRecordId() + 1).orElse(1);

        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setId(new RecordID(publisherId, recordId));
        recordEntity.setCaption(record.getCaption());
        recordEntity.setTimestamp(Instant.now().toString());
        String imgLocation = imageService.saveImage(image);
        recordEntity.setImgLocation(imgLocation);
        recordsRepo.save(recordEntity);

        return recordId;
    }

    @Override
    public ResponseRecord getRecordCore(RecordID id) {
        // todo: replace 1 huge load to RAM with 3 count queries to DB
        RecordEntity record = recordsRepo.findById(id).orElseThrow(() -> new NoSuchRecordException(id.getRecordId()));
        ResponseRecord res = new ResponseRecord();
        BeanUtils.copyProperties(record, res);
        res.setLikes(record.getLikeUsers().size());
        res.setDislikes(record.getDislikeUsers().size());
        res.setNumOfComments(record.getComments().size());
        return res;
    }

    @Override
    public String getImgLocation(RecordID id) {
        return recordsRepo.getImgLocation(id).orElseThrow(() -> new NoSuchRecordException(id.getRecordId()));
    }

    @Override
    public void editRecord(RecordID id, RequestRecord editRequest) {
        String caption = editRequest.getCaption();
        String adText = editRequest.getAdText();
        if (caption != null)
            if (adText != null)
                recordsRepo.updateRecord(id, caption, adText);
            else
                recordsRepo.updateCaption(id, caption);
        else if (adText != null)
            recordsRepo.updateAdText(id, adText);
        else
            throw new BlankRecordEditException("no update data provided");
    }


    @Override
    public void removeRecord(RecordID id) {
        Optional<RecordEntity> maybeRecord = recordsRepo.findById(id);
        maybeRecord.ifPresent(record -> {
            String imgPath = record.getImgLocation();
            if (!imageService.deleteImage(imgPath)) throw  new ServerLogicsError("record image missing");
            //todo: is it necessary that `commentsRepo` is injected?
            commentsRepo.deleteById_PublisherIdAndId_RecordId(id.getPublisherId(), id.getRecordId());
            recordsRepo.deleteById(id);
        });
    }
}
