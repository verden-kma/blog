package edu.ukma.blog.repositories;

import edu.ukma.blog.models.Evaluation;
import edu.ukma.blog.models.compositeIDs.EvaluatorId;
import edu.ukma.blog.models.compositeIDs.RecordId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEvaluatorsRepo extends JpaRepository<Evaluation, EvaluatorId> {
    Slice<Evaluation> findAllById_RecordIdAndIsLiker(RecordId id, Boolean isLiker, Pageable pageable);

    int countAllById_RecordIdAndIsLiker(RecordId recordId, Boolean isLiker);
}
