package edu.ukma.blog.services;

import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.models.record.evaluation.EvalPage;
import org.springframework.data.domain.Pageable;


public interface IRecordEvalService {
    Boolean getReaction(RecordId recordId, long userId);

    void putEvaluation(RecordId recordId, long userId, boolean eval);

    void removeEvaluation(RecordId recordId, long userId, boolean eval);

    EvalPage getLikers(RecordId recordId, Pageable pageable);

    EvalPage getDislikers(RecordId recordId, Pageable pageable);
}
