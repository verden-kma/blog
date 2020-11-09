package edu.ukma.blog.repositories;


import edu.ukma.blog.models.compositeIDs.EvaluatorId;
import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.models.record.evaluation.Evaluation;
import edu.ukma.blog.repositories.projections.MultiRecordEvalView;
import edu.ukma.blog.repositories.projections.RecordEvaluationView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface IEvaluatorsRepo extends JpaRepository<Evaluation, EvaluatorId> {
    Slice<Evaluation> findAllById_RecordIdAndIsLiker(RecordId id, Boolean isLiker, Pageable pageable);

    int countAllById_RecordIdAndIsLiker(RecordId recordId, Boolean isLiker);

    List<RecordEvaluationView> findAllById_EvaluatorOwnIdAndId_RecordId_PublisherIdAndId_RecordId_RecordOwnIdIn
            (long evaluatorOwnId, long publisherId, Collection<Integer> recordOwnId);

/*
SELECT
    record_own_id,
    is_liker,
    COUNT(CONCAT(evaluator_own_id,
            '-',
            publisher_id,
            '-',
            record_own_id)) AS count
FROM
    evaluation
WHERE
    record_own_id IN (:idList)
        AND is_liker IS NOT NULL
        AND publisher_id = :publisherId
GROUP BY record_own_id , is_liker
* */

    @Query(value = "SELECT \n" +
            "    record_own_id,\n" +
            "    is_liker,\n" +
            "    COUNT(CONCAT(evaluator_own_id,\n" +
            "            '-',\n" +
            "            publisher_id,\n" +
            "            '-',\n" +
            "            record_own_id)) AS mono_eval_count\n" +
            "FROM\n" +
            "    evaluation\n" +
            "WHERE\n" +
            "    record_own_id IN (:recordsOwnIds)\n" +
            "        AND is_liker IS NOT NULL\n" +
            "        AND publisher_id = :publisherId\n" +
            "GROUP BY record_own_id , is_liker", nativeQuery = true)
    List<MultiRecordEvalView> getRecordsEvaluations(@Param(value = "publisherId") long publisherId,
                                                    @Param(value = "recordsOwnIds") List<Integer> recordsOwnIds);
}
