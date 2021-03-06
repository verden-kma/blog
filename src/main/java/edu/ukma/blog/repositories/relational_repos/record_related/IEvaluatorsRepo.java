package edu.ukma.blog.repositories.relational_repos.record_related;


import edu.ukma.blog.models.composite_id.EvaluatorId;
import edu.ukma.blog.models.composite_id.RecordId;
import edu.ukma.blog.models.simple_interaction.Evaluation;
import edu.ukma.blog.repositories.relational_repos.projections.record.MultiRecordEvalView;
import edu.ukma.blog.repositories.relational_repos.projections.record.RecordEvaluationView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface IEvaluatorsRepo extends JpaRepository<Evaluation, EvaluatorId> {
    Slice<Evaluation> findAllById_RecordIdAndIsLiker(RecordId id, Boolean isLiker, Pageable pageable);

    int countAllById_RecordIdAndIsLiker(RecordId recordId, boolean isLiker);

    List<RecordEvaluationView> findAllById_EvaluatorUserIdAndId_RecordId_PublisherIdAndId_RecordId_RecordOwnIdIn
            (long evaluatorOwnId, long publisherId, Collection<Integer> recordOwnId);

    @Modifying
    void deleteById_RecordId(RecordId recordId);

    @Query(value = "SELECT \n" +
            "    record_own_id,\n" +
            "    is_liker,\n" +
            "    COUNT(*) AS mono_eval_count\n" +
            "FROM\n" +
            "    evaluation\n" +
            "WHERE\n" +
            "    publisher_id = :publisherId\n" +
            "        AND is_liker IS NOT NULL\n" +
            "        AND record_own_id IN (:recordsOwnIds)\n" +
            "GROUP BY record_own_id , is_liker", nativeQuery = true)
    List<MultiRecordEvalView> getRecordsEvaluations(@Param(value = "publisherId") long publisherId,
                                                    @Param(value = "recordsOwnIds") List<Integer> recordsOwnIds);

    /*
SELECT
    record_own_id,
    is_liker,
    COUNT(CONCAT(evaluator_user_id,
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
}
