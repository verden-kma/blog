package edu.ukma.blog.services.interfaces.record_related;

import edu.ukma.blog.models.composite_id.RecordId;
import edu.ukma.blog.utils.LazyContentPage;
import org.springframework.data.domain.Pageable;


public interface IRecordEvalService {
    Boolean getReaction(RecordId recordId, long userId);

    void putEvaluation(RecordId recordId, long userId, boolean eval);

    void removeEvaluation(RecordId recordId, long userId, boolean eval);

    LazyContentPage<String> getLikers(RecordId recordId, Pageable pageable);

    LazyContentPage<String> getDislikers(RecordId recordId, Pageable pageable);
}
