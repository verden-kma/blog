package edu.ukma.blog.services.interfaces.features;

import edu.ukma.blog.models.composite_id.RecordId;

import java.util.List;

public interface IRecommendService {
    List<Long> getSubscriptionRecoms(long clientId, int limit);

    List<RecordId> getRecordRecomsByRecord(RecordId recordId, int limit);

    List<RecordId> getRecordRecoms(long clientId, int limit);
}
