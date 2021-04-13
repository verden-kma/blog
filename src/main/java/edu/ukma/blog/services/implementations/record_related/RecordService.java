package edu.ukma.blog.services.implementations.record_related;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.Multimap;
import edu.ukma.blog.exceptions.record.NoSuchRecordException;
import edu.ukma.blog.exceptions.server_internal.ServerCriticalError;
import edu.ukma.blog.exceptions.server_internal.ServerLogicsError;
import edu.ukma.blog.exceptions.server_internal.WrongFileFormatException;
import edu.ukma.blog.models.composite_id.EvaluatorId;
import edu.ukma.blog.models.composite_id.RecordId;
import edu.ukma.blog.models.record.*;
import edu.ukma.blog.models.simple_interaction.Evaluation;
import edu.ukma.blog.models.simple_interaction.graph_models.RecordGraphEntity;
import edu.ukma.blog.repositories.ICommentsRepo;
import edu.ukma.blog.repositories.IEvaluatorsRepo;
import edu.ukma.blog.repositories.IPublisherStatsRepo;
import edu.ukma.blog.repositories.IRecordsRepo;
import edu.ukma.blog.repositories.graph_repos.IRecordNodesRepo;
import edu.ukma.blog.repositories.projections.record.MinRecordView;
import edu.ukma.blog.repositories.projections.record.RecordCommentsNumView;
import edu.ukma.blog.repositories.projections.record.RecordOwnIdView;
import edu.ukma.blog.services.interfaces.record_related.IRecordImageService;
import edu.ukma.blog.services.interfaces.record_related.IRecordService;
import edu.ukma.blog.services.interfaces.user_related.IUserService;
import edu.ukma.blog.utils.EagerContentPage;
import edu.ukma.blog.utils.LazyContentPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordService implements IRecordService {

    @PersistenceContext
    private EntityManager entityManager;

    private final IRecordsRepo recordsRepo;

    private final ICommentsRepo commentsRepo;

    private final IRecordImageService imageService;

    private final IEvaluatorsRepo evaluatorsRepo;

    private final IPublisherStatsRepo publisherStatsRepo;

    private final IUserService userService;

    private final IRecordNodesRepo recordNodesRepo;

    @Override
    @Transactional
    public int addRecord(long publisherId, RequestRecord record, MultipartFile image)
            throws ServerCriticalError, WrongFileFormatException {
        Optional<RecordOwnIdView> lastRecord = recordsRepo.findTopById_PublisherIdOrderById_RecordOwnIdDesc(publisherId);
        int recordId = lastRecord.map(value -> value.getId().getRecordOwnId() + 1).orElse(1);

        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setId(new RecordId(publisherId, recordId));
        BeanUtils.copyProperties(record, recordEntity);
        recordEntity.setTimestamp(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        String imgLocation = imageService.saveImage(image);
        recordEntity.setImgLocation(imgLocation);
        recordsRepo.save(recordEntity);

        RecordGraphEntity rge = new RecordGraphEntity(recordEntity.getId());
        recordNodesRepo.save(rge);

        publisherStatsRepo.incUploadsCount(publisherId);
        return recordId;
    }

    @Override
    public EagerContentPage<ResponseRecord> getRecordsPage(long publisherId, long userId, Pageable pageable) {
        int numPages = (int) Math.ceil((double) recordsRepo.countAllById_PublisherId(publisherId) / pageable.getPageSize());
        List<RecordEntity> pageRecords = recordsRepo.findAllById_PublisherId(publisherId, pageable);
        List<List<RecordEntity>> samePubRecEnts = new ArrayList<>(1);
        samePubRecEnts.add(pageRecords);
        return new EagerContentPage<>(buildRespRecs(samePubRecEnts, userId), numPages);
    }

    public List<ResponseRecord> buildRespRecs(Collection<List<RecordEntity>> recordsChunks, long userId) {
        int resSize = 0;
        for (List<RecordEntity> records : recordsChunks)
            resSize += records.size();

        List<ResponseRecord> res = new ArrayList<>(resSize);

        BiMap<Long, String> userIdMap = userService.getUserIdentifiersBimap(recordsChunks
                .stream()
                .flatMap(Collection::stream)
                .map(x -> x.getId().getPublisherId()).collect(Collectors.toList()));

        for (List<RecordEntity> records : recordsChunks) {
            if (records.isEmpty()) continue;
            long publisherId = records.get(0).getId().getPublisherId();

            List<ResponseRecord> respRecs = records.stream().map(x -> {
                ResponseRecord resp = new ResponseRecord();
                BeanUtils.copyProperties(x, resp);
                resp.setPublisher(userIdMap.get(x.getId().getPublisherId()));
                resp.setId(x.getId().getRecordOwnId());
                return resp;
            }).collect(Collectors.toList());

            List<Integer> recordOwnIds = records.stream()
                    .map(x -> x.getId().getRecordOwnId())
                    .collect(Collectors.toList());

            // get all (encapsulated in projection interfaces) recordIds associated with evaluations (maybe) made by a user
            // take data out of encapsulation noise
            Map<Integer, Boolean> userEvals =
                    evaluatorsRepo.findAllById_EvaluatorUserIdAndId_RecordId_PublisherIdAndId_RecordId_RecordOwnIdIn
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

            Map<Integer, Integer> commentsNum = commentsRepo.getCommentsNumForRecords(publisherId, recordOwnIds)
                    .stream()
                    .collect(Collectors.toMap(RecordCommentsNumView::getRecord_Own_Id,
                            RecordCommentsNumView::getComment_Count));

            for (ResponseRecord respRec : respRecs) {
                respRec.setReaction(userEvals.get(respRec.getId()));
                // for i in range [0, 2)
                for (Pair<Boolean, Integer> pair : recordsEvaluations.get(respRec.getId())) {
                    if (pair.getFirst()) respRec.setLikes(pair.getSecond());
                    else respRec.setDislikes(pair.getSecond());
                }

                if (commentsNum.containsKey(respRec.getId()))
                    respRec.setNumOfComments(commentsNum.get(respRec.getId()));
            }
            res.addAll(respRecs);
        }
        return res;
    }

    @Override
    public LazyContentPage<MinResponseRecord> getMinResponsePage(Pageable pageable) {
        Slice<MinRecordView> minRecs = recordsRepo.findAllBy(pageable);
        List<Long> userIds = minRecs.stream().map(x -> x.getId().getPublisherId()).collect(Collectors.toList());
        final BiMap<Long, String> userBiIds = userService.getUserIdentifiersBimap(userIds);

        List<MinResponseRecord> minRespRecs = minRecs.stream().map(x -> {
            String username = userBiIds.get(x.getId().getPublisherId());
            return new MinResponseRecord(username, x.getId().getRecordOwnId(), x.getCaption());
        }).collect(Collectors.toList());

        return new LazyContentPage<>(minRespRecs, minRecs.isLast());
    }

    @Override
    public ResponseRecord getRecordCore(RecordId recordId, long userId) {
        RecordEntity record = recordsRepo.findById(recordId)
                .orElseThrow(() -> new NoSuchRecordException(recordId.getRecordOwnId()));
        ResponseRecord res = new ResponseRecord();
        res.setPublisher(userService.getUsernamesByIds(Collections.singletonList(recordId.getPublisherId())).get(0));
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
        return recordsRepo.findImgLocationById(id)
                .orElseThrow(() -> new NoSuchRecordException(id.getRecordOwnId()))
                .getImgLocation();
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
        criteriaUpdate.set(root.get(RecordEntity_.isEdited), true)
                .where(cb.equal(root.get(RecordEntity_.id), id));

        entityManager.createQuery(criteriaUpdate).executeUpdate();
    }

    @Override
    @Transactional
    public void removeRecord(RecordId id) {
        Optional<RecordEntity> maybeRecord = recordsRepo.findById(id);
        maybeRecord.ifPresent(record -> {
            String imgPath = record.getImgLocation();
            if (!imageService.deleteImage(imgPath)) throw new ServerLogicsError("record image missing");
            int numLikes = evaluatorsRepo.countAllById_RecordIdAndIsLiker(id, true);
            int numDislikes = evaluatorsRepo.countAllById_RecordIdAndIsLiker(id, false);
            int numComments = commentsRepo.countAllById_RecordId(id);
            publisherStatsRepo.removeRecordStats(id.getPublisherId(), numLikes, numDislikes, numComments);
            evaluatorsRepo.deleteById_RecordId(id);
            commentsRepo.deleteById_RecordId(id);
            recordsRepo.deleteById(id);

            recordNodesRepo.deleteByPublisherIdAndRecordOwnId(id.getPublisherId(), id.getRecordOwnId());
        });
    }

    @Override
    public List<MinResponseRecord> getSelectedMinResponse(String publisher, List<Integer> rids) {
        long pid = userService.getUserIdByUsername(publisher);
        return recordsRepo.findByIdPublisherIdAndIdRecordOwnIdIn(pid, rids).stream()
                .map(view -> new MinResponseRecord(publisher, view.getId().getRecordOwnId(), view.getCaption()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResponseRecord> getResponseByIds(List<RecordId> recordIdList, long userId) {
        return buildRespRecs(Collections.singletonList(recordsRepo.findAllById(recordIdList)), userId);
    }
}
