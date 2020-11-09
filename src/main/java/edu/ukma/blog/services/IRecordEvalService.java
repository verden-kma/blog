package edu.ukma.blog.services;

import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.models.record.evaluation.EvalPage;
import org.springframework.data.domain.Pageable;


public interface IRecordEvalService {
    Boolean getReaction(RecordId recordId, long userId);

    void putLike(RecordId recordId, long userId);

    void removeLike(RecordId recordId, long userId);

    EvalPage getLikers(RecordId recordId, Pageable pageable);

    void putDislike(RecordId recordId, long userId);

    void removeDislike(RecordId recordId, long userId);

    EvalPage getDislikers(RecordId recordId, Pageable pageable);
}
