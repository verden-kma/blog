package edu.ukma.blog.services;

import edu.ukma.blog.models.compositeIDs.RecordId;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRecommendService {
    List<Long> getSubscriptionRecommendations(long clientId, Pageable pageable);

    List<RecordId> getRecordRecommendations(long clientId, Pageable pageable);
}
