package edu.ukma.blog.repositories;


import edu.ukma.blog.models.user.PublisherStats;
import edu.ukma.blog.models.user.UserEntity;
import edu.ukma.blog.repositories.projections.user.PublisherPreviewBaseView;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ContextConfiguration(classes = JpaConfig.class, loader = AnnotationConfigContextLoader.class)
public class UsersRepoTest {
    @Resource
    private IUsersRepo usersRepo;

    @Test
    void testFindPopularPublishersWithUsernamePrefix() {
        UserEntity u1 = new UserEntity();
        u1.setId(1);
        u1.setUsername("tu1");
        u1.setEncryptedPassword("placeholder");
        PublisherStats ps1 = new PublisherStats(u1);
        ps1.setUploads(5);
        ps1.setFollowers(15);
        u1.setStatistics(ps1);

        UserEntity u2 = new UserEntity();
        u2.setId(2);
        u2.setUsername("tu2");
        u2.setEncryptedPassword("placeholder");
        PublisherStats ps2 = new PublisherStats(u2);
        ps2.setUploads(50);
        ps2.setFollowers(47);
        u2.setStatistics(ps2);

        UserEntity u3 = new UserEntity();
        u3.setId(3);
        u3.setUsername("abc");
        u3.setEncryptedPassword("placeholder");
        PublisherStats ps3 = new PublisherStats(u3);
        ps3.setUploads(80);
        ps3.setFollowers(1140);
        u3.setStatistics(ps3);

        UserEntity u4 = new UserEntity();
        u4.setId(4);
        u4.setUsername("tu4");
        u4.setEncryptedPassword("placeholder");
        PublisherStats ps4 = new PublisherStats(u4);
        ps4.setUploads(1);
        ps4.setFollowers(2);
        u4.setStatistics(ps4);

        UserEntity u5 = new UserEntity();
        u5.setId(5);
        u5.setUsername("tu5");
        u5.setEncryptedPassword("placeholder");
        PublisherStats ps5 = new PublisherStats(u5);
        ps5.setUploads(34);
        ps5.setFollowers(783);
        u5.setStatistics(ps5);

        usersRepo.saveAll(Lists.list(u1, u2, u3, u4, u5));

        List<PublisherPreviewBaseView> res = usersRepo.findPopularPublishersWithUsernamePrefix("tu",
                PageRequest.of(0, 2)).toList();

        assertEquals(2, res.size());

        assertEquals(u5.getId(), res.get(0).getId());
        assertEquals(u5.getUsername(), res.get(0).getUsername());
        assertEquals(u5.getStatistics().getFollowers(), res.get(0).getStatistics().getFollowers());
        assertEquals(u5.getStatistics().getUploads(), res.get(0).getStatistics().getUploads());

        assertEquals(u2.getId(), res.get(1).getId());
        assertEquals(u2.getUsername(), res.get(1).getUsername());
        assertEquals(u2.getStatistics().getFollowers(), res.get(1).getStatistics().getFollowers());
        assertEquals(u2.getStatistics().getUploads(), res.get(1).getStatistics().getUploads());
    }
}
