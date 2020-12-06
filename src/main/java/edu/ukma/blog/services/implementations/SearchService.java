package edu.ukma.blog.services.implementations;

import edu.ukma.blog.models.record.RecordEntity;
import edu.ukma.blog.models.record.ResponseRecord;
import edu.ukma.blog.models.user.responses.PublisherPreview;
import edu.ukma.blog.repositories.IFollowersRepo;
import edu.ukma.blog.repositories.IRecordsRepo;
import edu.ukma.blog.repositories.IUsersRepo;
import edu.ukma.blog.repositories.projections.record.RecordImgLocationAndPublisherIdView;
import edu.ukma.blog.repositories.projections.user.PublisherPreviewBaseView;
import edu.ukma.blog.services.IRecordService;
import edu.ukma.blog.services.ISearchService;
import edu.ukma.blog.utils.LazyContentPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService implements ISearchService {

    @Autowired
    private IUsersRepo usersRepo;

    @Autowired
    private IRecordsRepo recordsRepo;

    @Autowired
    private IRecordService recordService;

    @Autowired
    private IFollowersRepo followersRepo;

    @Override
    public LazyContentPage<PublisherPreview> findPopularPublishers(String prefix, Pageable publisherPageable,
                                                                   long userId, int numPreviewImgs) {
        Slice<PublisherPreviewBaseView> previewBasesSlice = usersRepo.findPopularPublishersWithUsernamePrefix(prefix, publisherPageable);
        List<PublisherPreviewBaseView> previewBases = previewBasesSlice.getContent();

        List<Long> publisherIds = previewBases.stream().map(PublisherPreviewBaseView::getId).collect(Collectors.toList());
        List<RecordImgLocationAndPublisherIdView> publishersRecentRecordsImgPathsAndIdsViews =
                recordsRepo.getLastRecordsOfPublishers(publisherIds, numPreviewImgs);

        Map<Long, List<String>> publisherRecentImgs = new HashMap<>(publishersRecentRecordsImgPathsAndIdsViews.size());
        for (RecordImgLocationAndPublisherIdView prv : publishersRecentRecordsImgPathsAndIdsViews)
            publisherRecentImgs.compute(prv.getPublisher_Id(), (id, imgs) -> {
                if (imgs == null) imgs = new ArrayList<>(numPreviewImgs);
                imgs.add(prv.getImg_Location());
                return imgs;
            });

        assert (previewBases.size() == publisherRecentImgs.size());

        Set<Long> subs = followersRepo.findById_SubscriberIdAndId_PublisherIdIn(userId, publisherIds)
                .stream()
                .map(x -> x.getId().getSubscriber())
                .collect(Collectors.toSet());

        List<PublisherPreview> res = new ArrayList<>(previewBases.size());
        for (PublisherPreviewBaseView pb : previewBases) {
            PublisherPreview pp = new PublisherPreview();
            pp.setPublisherName(pb.getUsername());
            pp.setUploads(pb.getStatistics().getUploads());
            pp.setFollowers(pb.getStatistics().getFollowers());
            pp.setFollowed(subs.contains(pb.getId()));
            pp.setLastRecordsImgPaths(publisherRecentImgs.get(pb.getId()));
            res.add(pp);
        }
        return new LazyContentPage<>(res, previewBasesSlice.isLast());
    }

    @Override
    public LazyContentPage<ResponseRecord> findRecordsWithTitleLike(String titleSubstr, Pageable pageable, long userId) {
        Slice<RecordEntity> recEnts = recordsRepo.findByCaptionContains(titleSubstr, pageable);
        Map<Long, List<RecordEntity>> samePublisherRecords = recEnts.stream()
                .collect(Collectors.groupingBy(x -> x.getId().getPublisherId()));
        return new LazyContentPage<>(recordService.buildRespRecs(samePublisherRecords.values(), userId), recEnts.isLast());
    }
}
