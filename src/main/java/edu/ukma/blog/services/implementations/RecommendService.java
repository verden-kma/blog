package edu.ukma.blog.services.implementations;

import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.repositories.graph_repos.IRecordNodesRepo;
import edu.ukma.blog.repositories.graph_repos.IUserNodesRepo;
import edu.ukma.blog.repositories.graph_repos.graph_projections.UserRecomView;
import edu.ukma.blog.services.IRecommendService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public List<Long> getSubscriptionRecommendations(long clientId, Pageable pageable) {
        return userNodesRepo.getRecommendations(clientId, pageable)
                .stream()
                .map(UserRecomView::getRecommendation)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecordId> getRecordRecommendations(long clientId, Pageable pageable) {
        // todo: figure out how to write a single query that gets the job done
        class RecordRecommendation {
            RecordId recordId;
            int strength;
        }

        List<RecordId> recoms = recordNodesRepo.getRecordsRecoms(clientId, pageable)
                .stream()
                .map(x -> new RecordId(x.getPublisherId(), x.getRecordOwnId()))
                .collect(Collectors.toList());

        return recoms;
    }
}
