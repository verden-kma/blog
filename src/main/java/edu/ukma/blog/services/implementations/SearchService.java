package edu.ukma.blog.services.implementations;

import edu.ukma.blog.models.record.ResponseRecord;
import edu.ukma.blog.models.user.responses.PublisherPreview;
import edu.ukma.blog.repositories.IRecordsRepo;
import edu.ukma.blog.repositories.IUsersRepo;
import edu.ukma.blog.repositories.projections.record.RecordImgLocationAndPublisherIdView;
import edu.ukma.blog.repositories.projections.user.PublisherPreviewBaseView;
import edu.ukma.blog.services.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchService implements ISearchService {

    @Autowired
    private IUsersRepo usersRepo;

    @Autowired
    private IRecordsRepo recordsRepo;

    @Override
    public List<PublisherPreview> findPopularPublishers(String prefix, Pageable publisherPageable, int numPreviewImgs) {
        List<PublisherPreviewBaseView> previewBases = usersRepo.findPopularPublishersWithUsernamePrefix(prefix, publisherPageable);
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

        List<PublisherPreview> res = new ArrayList<>(previewBases.size());
        for (PublisherPreviewBaseView pb : previewBases) {
            PublisherPreview pp = new PublisherPreview();
            pp.setUsername(pb.getUsername());
            pp.setUploads(pb.getStatistics().getUploads());
            pp.setFollowers(pb.getStatistics().getFollowers());
            pp.setLastRecordsImgPaths(publisherRecentImgs.get(pb.getId()));
            res.add(pp);
        }
        return res;
    }

    @Override
    public List<ResponseRecord> findRecords(String substr, Pageable pageable) {
        return null;
    }
}
