package edu.ukma.blog.services.implementations.features;

import edu.ukma.blog.models.composite_id.RecordId;
import edu.ukma.blog.repositories.graph_repos.IRecordNodesRepo;
import edu.ukma.blog.repositories.graph_repos.IUserNodesRepo;
import edu.ukma.blog.repositories.graph_repos.graph_projections.RecordRecomView;
import edu.ukma.blog.repositories.graph_repos.graph_projections.UserRecomView;
import edu.ukma.blog.services.interfaces.features.IRecommendService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecommendService implements IRecommendService {
    private final IUserNodesRepo userNodesRepo;

    private final IRecordNodesRepo recordNodesRepo;

    public RecommendService(IUserNodesRepo userNodesRepo, IRecordNodesRepo recordNodesRepo) {
        this.userNodesRepo = userNodesRepo;
        this.recordNodesRepo = recordNodesRepo;
    }

    @Override
    public List<Long> getSubscriptionRecoms(long clientId, int limit) {
        return userNodesRepo.getRecommendations(clientId, limit)
                .stream()
                .map(UserRecomView::getRecommendation)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecordId> getRecordRecomsByRecord(RecordId recordId, int limit) {
        return recordNodesRepo.getRecordRecomsSimilarToRecord(recordId.getPublisherId(), recordId.getRecordOwnId(), limit)
                .stream()
                .map(x -> new RecordId(x.getPublisherId(), x.getRecordOwnId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<RecordId> getRecordRecoms(long clientId, int limit) {
        List<RecordRecomView> recomViews = recordNodesRepo.getRecordsRecoms(clientId, limit);
        List<String> recomIds = recomViews.stream().map(RecordRecomView::getUuid).collect(Collectors.toList());

        Map<RecordId, Integer> recoms = recomViews.stream().collect(Collectors.toMap(
                (x) -> new RecordId(x.getPublisherId(), x.getRecordOwnId()), RecordRecomView::getStrength));

        // NullPointerException cannot be thrown as only counter recommendations for potential (present)
        // recommendations are returned by the query
        recordNodesRepo.getRecordCounterRecoms(clientId, recomIds)
                .forEach(x -> recoms.compute(new RecordId(x.getPublisherId(), x.getRecordOwnId()),
                        (key, strengthFor) -> strengthFor - x.getStrength()));

        List<Map.Entry<RecordId, Integer>> recs = new ArrayList<>(recoms.entrySet());
        recs.sort(Map.Entry.comparingByValue((e1, e2) -> e2 - e1));

        return recs.stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }
}
