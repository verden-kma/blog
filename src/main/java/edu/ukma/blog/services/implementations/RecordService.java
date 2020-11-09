package edu.ukma.blog.services.implementations;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import edu.ukma.blog.exceptions.record.NoSuchRecordException;
import edu.ukma.blog.exceptions.server_internal.ServerCriticalError;
import edu.ukma.blog.exceptions.server_internal.ServerLogicsError;
import edu.ukma.blog.exceptions.server_internal.WrongFileFormatException;
import edu.ukma.blog.models.compositeIDs.EvaluatorId;
import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.models.record.*;
import edu.ukma.blog.models.record.evaluation.Evaluation;
import edu.ukma.blog.repositories.ICommentsRepo;
import edu.ukma.blog.repositories.IEvaluatorsRepo;
import edu.ukma.blog.repositories.IRecordsRepo;
import edu.ukma.blog.repositories.projections.RecordCommentsNumView;
import edu.ukma.blog.services.IRecordImageService;
import edu.ukma.blog.services.IRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecordService implements IRecordService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private IRecordsRepo recordsRepo;

    @Autowired
    private ICommentsRepo commentsRepo;

    @Autowired
    private IRecordImageService imageService;

    @Autowired
    private IEvaluatorsRepo evaluatorsRepo;

    @Override
    public int addRecord(long publisherId, RequestRecord record, MultipartFile image)
            throws ServerCriticalError, WrongFileFormatException {
        Optional<RecordEntity> lastRecord = recordsRepo.findTopById_PublisherIdOrderById_RecordOwnIdDesc(publisherId);
        int recordId = lastRecord.map(value -> value.getId().getRecordOwnId() + 1).orElse(1);

        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setId(new RecordId(publisherId, recordId));
        recordEntity.setCaption(record.getCaption());
        recordEntity.setTimestamp(Instant.now().toString());
        String imgLocation = imageService.saveImage(image);
        recordEntity.setImgLocation(imgLocation);
        recordsRepo.save(recordEntity);

        return recordId;
    }

    @Override
    public RecordsPage getRecordsPage(long publisherId, long userId, Pageable pageable) {
        int numPages = (int) Math.ceil((double) recordsRepo.countAllById_PublisherId(publisherId) / pageable.getPageSize());
        List<RecordEntity> pageRecords = recordsRepo.findAllById_PublisherId(publisherId, pageable);

        List<ResponseRecord> respRecs = pageRecords.stream().map(x -> {
            ResponseRecord resp = new ResponseRecord();
            BeanUtils.copyProperties(x, resp);
            resp.setId(x.getId().getRecordOwnId());
            return resp;
        }).collect(Collectors.toList());

        List<Integer> recordOwnIds = pageRecords.stream().map(x -> x.getId().getRecordOwnId()).collect(Collectors.toList());

        // get all (encapsulated in projection interfaces) recordIds associated with evaluations (maybe) made by a user
        // take data out of encapsulation noise
        Map<Integer, Boolean> userEvals =
                evaluatorsRepo.findAllById_EvaluatorOwnIdAndId_RecordId_PublisherIdAndId_RecordId_RecordOwnIdIn
                        (userId, publisherId, recordOwnIds)
                        .stream()
                        .collect(HashMap::new,
                                (m, proj) -> m.put(proj.getId().getRecordId().getRecordOwnId(), proj.getIsLiker()),
                                HashMap::putAll);

        // associate each recordOwnId with pairs<isLike, numOfMarks>
        // i.e. for each record get a pair of likes and dislikes quantities
        Multimap<Integer, Pair<Boolean, Integer>> recordsEvaluations =
                evaluatorsRepo.getRecordsEvaluations(publisherId, recordOwnIds)
                        .stream()
                        .collect(ArrayListMultimap::create, (map, entry) -> map.put(entry.getRecord_Own_Id(),
                                Pair.of(entry.getIs_Liker(), entry.getMono_Eval_Count())), Multimap::putAll);

        // todo: set num of comments
        Map<Integer, Integer> commentsNum = commentsRepo.getCommentsNumForRecords(publisherId, recordOwnIds)
                .stream()
                .collect(Collectors.toMap(RecordCommentsNumView::getRecord_Own_Id,
                        RecordCommentsNumView::getComment_Count));

        for (ResponseRecord respRec : respRecs) {
            respRec.setReaction(userEvals.get(respRec.getId()));
            // for i in range [0, 2]
            for (Pair<Boolean, Integer> pair : recordsEvaluations.get(respRec.getId())) {
                if (pair.getFirst()) respRec.setLikes(pair.getSecond());
                else respRec.setDislikes(pair.getSecond());
            }

            if (commentsNum.containsKey(respRec.getId()))
                respRec.setNumOfComments(commentsNum.get(respRec.getId()));
        }

        return new RecordsPage(respRecs, numPages);
    }

    @Override
    public ResponseRecord getRecordCore(RecordId recordId, long userId) {
        RecordEntity record = recordsRepo.findById(recordId)
                .orElseThrow(() -> new NoSuchRecordException(recordId.getRecordOwnId()));
        ResponseRecord res = new ResponseRecord();
        res.setId(recordId.getRecordOwnId());
        BeanUtils.copyProperties(record, res);
        res.setLikes(evaluatorsRepo.countAllById_RecordIdAndIsLiker(recordId, true));
        res.setDislikes(evaluatorsRepo.countAllById_RecordIdAndIsLiker(recordId, false));
        res.setReaction(evaluatorsRepo.findById(new EvaluatorId(recordId, userId))
                .map(Evaluation::getIsLiker)
                .orElse(null));

        res.setNumOfComments(commentsRepo.countAllById_RecordId(recordId));
        return res;
    }

    @Override
    public String getImgLocation(RecordId id) {
        return recordsRepo.getImgLocation(id).orElseThrow(() -> new NoSuchRecordException(id.getRecordOwnId()));
    }

    @Override
    @Transactional
    public void editRecord(RecordId id, RequestRecord editRequest) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<RecordEntity> criteriaUpdate = cb.createCriteriaUpdate(RecordEntity.class);
        Root<RecordEntity> root = criteriaUpdate.from(RecordEntity.class);
        if (editRequest.getCaption() != null) {
            criteriaUpdate.set(root.get(RecordEntity_.caption), editRequest.getCaption())
                    .where(cb.equal(root.get(RecordEntity_.id), id));
        }
        if (editRequest.getAdText() != null) {
            criteriaUpdate.set(root.get(RecordEntity_.adText), editRequest.getAdText())
                    .where(cb.equal(root.get(RecordEntity_.id), id));
        }

        entityManager.createQuery(criteriaUpdate).executeUpdate();
    }


    @Override
    public void removeRecord(RecordId id) {
        Optional<RecordEntity> maybeRecord = recordsRepo.findById(id);
        maybeRecord.ifPresent(record -> {
            String imgPath = record.getImgLocation();
            if (!imageService.deleteImage(imgPath)) throw new ServerLogicsError("record image missing");
            commentsRepo.deleteById_RecordId(id);
            recordsRepo.deleteById(id);
        });
    }
}
