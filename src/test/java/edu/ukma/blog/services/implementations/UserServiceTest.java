package edu.ukma.blog.services.implementations;

import edu.ukma.blog.models.composite_id.FollowerId;
import edu.ukma.blog.models.user.PublisherStats;
import edu.ukma.blog.models.user.UserEntity;
import edu.ukma.blog.models.user.responses.UserDataResponse;
import edu.ukma.blog.repositories.IFollowersRepo;
import edu.ukma.blog.repositories.IUsersRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    IUsersRepo usersRepo;

    @Mock
    IFollowersRepo followersRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetPublisher() {
        UserEntity ue = new UserEntity();
        ue.setId(1L);
        ue.setUsername("testUser");
        ue.setStatus("chyzhyk");
        ue.setDescription("drank water from a fontaine");
        PublisherStats ps = new PublisherStats();
        ps.setUploads(2);
        ps.setLikes(3);
        ps.setDislikes(4);
        ps.setFollowers(5);
        ps.setComments(6);
        ue.setStatistics(ps);

        when(usersRepo.findByUsername(anyString())).thenReturn(ue);
        when(usersRepo.getByUsername(anyString())).thenReturn(Optional.of(() -> 1L));
        when(followersRepo.existsById(any(FollowerId.class))).thenReturn(true);

        UserDataResponse uds = userService.getPublisher("", "");
        assertNotNull(uds);
        assertEquals(ue.getUsername(), uds.getUsername());
        assertEquals(ue.getStatus(), uds.getStatus());
        assertEquals(ue.getDescription(), uds.getDescription());
        assertEquals(ue.getStatistics(), ps);
    }


}
