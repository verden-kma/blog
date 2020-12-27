package edu.ukma.blog.repositories;

import edu.ukma.blog.repositories.projections.record.RecordCommentsNumView;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.annotation.Resource;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ContextConfiguration(classes = JpaConfig.class, loader = AnnotationConfigContextLoader.class)
@Sql({"/test_data/comments-data.sql"})
public class CommentsRepoTest {
    @Resource
    private ICommentsRepo commentsRepo;

    @Test
    void testGetCommentsNumForRecords() {
        Map<Integer, Integer> res = commentsRepo.getCommentsNumForRecords(1L, Lists.list(1, 2, 3))
                .stream().collect(Collectors.toMap(RecordCommentsNumView::getRecord_Own_Id,
                        RecordCommentsNumView::getComment_Count));

        assertEquals(3, res.size());
        assertEquals(3, res.get(1));
        assertEquals(2, res.get(2));
        assertEquals(2, res.get(3));
    }
}
