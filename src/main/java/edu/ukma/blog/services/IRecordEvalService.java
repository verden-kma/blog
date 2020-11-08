package edu.ukma.blog.services;

import edu.ukma.blog.models.compositeIDs.RecordId;

import java.util.List;

public interface IRecordEvalService {
    Boolean getReaction(RecordId recordId, long userId);

    void putLike(RecordId recordId, long userId);

    void removeLike(RecordId recordId, long userId);

    List<String> getLikers(RecordId recordId);

    void putDislike(RecordId recordId, long userId);

    void removeDislike(RecordId recordId, long userId);

    List<String> getDislikers(RecordId recordId);
}
