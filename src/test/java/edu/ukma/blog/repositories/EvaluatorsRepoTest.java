package edu.ukma.blog.repositories;

import edu.ukma.blog.repositories.relational_repos.projections.record.MultiRecordEvalView;
import edu.ukma.blog.repositories.relational_repos.record_related.IEvaluatorsRepo;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ContextConfiguration(classes = JpaConfig.class, loader = AnnotationConfigContextLoader.class)
@Sql({"/test_data/evaluators-data.sql"})
public class EvaluatorsRepoTest {
    @Resource
    private IEvaluatorsRepo evaluatorsRepo;

    @Test
    void testGetRecordsEvaluations() {
        List<MultiRecordEvalView> res = evaluatorsRepo.getRecordsEvaluations(10L, Lists.list(1, 2, 3));
        Map<Pair<Integer, Boolean>, Integer> grouped = res.stream()
                .collect(Collectors.toMap(entry -> Pair.of(entry.getRecord_Own_Id(),
                        entry.getIs_Liker()), MultiRecordEvalView::getMono_Eval_Count));

        assertEquals(5, res.size());

        assertEquals(3, grouped.get(Pair.of(1, true)));
        assertEquals(2, grouped.get(Pair.of(2, true)));
        assertEquals(1, grouped.get(Pair.of(2, false)));
        assertEquals(1, grouped.get(Pair.of(3, true)));
        assertEquals(2, grouped.get(Pair.of(3, false)));
    }

}
