package edu.ukma.blog.services.implementations.features;

import edu.ukma.blog.models.record.RecordEntity;
import edu.ukma.blog.models.record.ResponseRecord;
import edu.ukma.blog.models.user.responses.UserDataPreviewResponse;
import edu.ukma.blog.repositories.relational_repos.projections.record.RecordIdView;
import edu.ukma.blog.repositories.relational_repos.projections.user.PublisherPreviewBaseView;
import edu.ukma.blog.repositories.relational_repos.record_related.IRecordsRepo;
import edu.ukma.blog.repositories.relational_repos.user_related.IFollowersRepo;
import edu.ukma.blog.repositories.relational_repos.user_related.IUsersRepo;
import edu.ukma.blog.services.interfaces.features.ISearchService;
import edu.ukma.blog.services.interfaces.record_related.IRecordService;
import edu.ukma.blog.utils.EagerContentPage;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService implements ISearchService {

    private final IUsersRepo usersRepo;

    private final IRecordsRepo recordsRepo;

    private final IRecordService recordService;

    private final IFollowersRepo followersRepo;

    public SearchService(IUsersRepo usersRepo, IRecordsRepo recordsRepo, IRecordService recordService, IFollowersRepo followersRepo) {
        this.usersRepo = usersRepo;
        this.recordsRepo = recordsRepo;
        this.recordService = recordService;
        this.followersRepo = followersRepo;
    }

    @Override
    public EagerContentPage<UserDataPreviewResponse> findPublishersWithPrefix(String prefix, Pageable pageable,
                                                                              long userId, int numPreviewImgs) {
        List<PublisherPreviewBaseView> previewBases = usersRepo.findPopularPublishersWithUsernamePrefix(prefix, pageable);
        int numPages = (int) Math.ceil((double) usersRepo.countAllByUsernameStartingWithIgnoreCase(prefix) / pageable.getPageSize());

        List<Long> publisherIds = previewBases.stream().map(PublisherPreviewBaseView::getId).collect(Collectors.toList());
        List<RecordIdView> publishersRecentRecordsViews =
                recordsRepo.getLastRecordsOfPublishers(publisherIds, numPreviewImgs);

        Map<Long, List<Integer>> publisherRecentRecs = new HashMap<>(publishersRecentRecordsViews.size());
        for (RecordIdView prv : publishersRecentRecordsViews)
            publisherRecentRecs.compute(prv.getPublisher_Id(), (id, imgs) -> {
                if (imgs == null) imgs = new ArrayList<>(numPreviewImgs);
                imgs.add(prv.getRecord_Own_Id());
                return imgs;
            });

        Set<Long> subscriptions = followersRepo.findById_SubscriberIdAndId_PublisherIdIn(userId, publisherIds)
                .stream()
                .map(x -> x.getId().getPublisherId())
                .collect(Collectors.toSet());

        List<UserDataPreviewResponse> res = new ArrayList<>(previewBases.size());
        for (PublisherPreviewBaseView pb : previewBases) {
            UserDataPreviewResponse pp = new UserDataPreviewResponse();
            pp.setPublisher(pb.getUsername());
            pp.setUploads(pb.getStatistics().getUploads());
            pp.setFollowers(pb.getStatistics().getFollowers());
            pp.setFollowed(subscriptions.contains(pb.getId()));
            pp.setLastRecords(publisherRecentRecs.get(pb.getId()));
            res.add(pp);
        }
        return new EagerContentPage<>(res, numPages);
    }

    @Override
    public EagerContentPage<ResponseRecord> findRecordsWithTitleLike(String titleSubstr, Pageable pageable, long userId) {
        List<RecordEntity> recEnts = recordsRepo.findByCaptionContains(titleSubstr, pageable);
        int numPages = (int) Math.ceil((double) recordsRepo.countAllByCaptionContains(titleSubstr) / pageable.getPageSize());
        Map<Long, List<RecordEntity>> samePublisherRecords = recEnts.stream()
                .collect(Collectors.groupingBy(x -> x.getId().getPublisherId()));
        return new EagerContentPage<>(recordService.buildRespRecs(samePublisherRecords.values(), userId), numPages);
    }
}