package edu.ukma.blog.repositories;

import edu.ukma.blog.models.Evaluation;
import edu.ukma.blog.models.compositeIDs.EvaluatorId;
import edu.ukma.blog.models.compositeIDs.RecordId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEvaluatorsRepo extends JpaRepository<Evaluation, EvaluatorId> {
    List<Evaluation> findAllById_RecordIdAndIsLiker(RecordId id, Boolean isLiker);

    int countAllById_RecordIdAndIsLiker(RecordId recordId, Boolean isLiker);
}
