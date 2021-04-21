package edu.ukma.blog.repositories;


import edu.ukma.blog.models.composite_id.RecordId;
import edu.ukma.blog.repositories.relational_repos.record_related.IRecordsRepo;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ContextConfiguration(classes = JpaConfig.class, loader = AnnotationConfigContextLoader.class)
@Sql({"/test_data/records-data.sql"})
public class RecordsRepoTest {
    @Resource
    private IRecordsRepo recordsRepo;

    @Test
    void testGetLastRecordsOfPublishers() {
        List<RecordId> recIds = recordsRepo.getLastRecordsOfPublishers(Lists.list(1L, 2L, 3L), 3)
                .stream()
                .map(x -> new RecordId(x.getPublisher_Id(), x.getRecord_Own_Id()))
                .collect(Collectors.toList());

        assertEquals(6, recIds.size());
        assertTrue(recIds.contains(new RecordId(1L, 1)));

        assertTrue(recIds.contains(new RecordId(2L, 1)));
        assertTrue(recIds.contains(new RecordId(2L, 2)));

        assertTrue(recIds.contains(new RecordId(3L, 2)));
        assertTrue(recIds.contains(new RecordId(3L, 3)));
        assertTrue(recIds.contains(new RecordId(3L, 4)));
    }
}
