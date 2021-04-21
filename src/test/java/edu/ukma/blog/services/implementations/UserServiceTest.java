package edu.ukma.blog.services.implementations;

// yeap criminal is who I am

public class UserServiceTest {
//
//    @InjectMocks
//    UserService userService;
//
//    @Mock
//    IUsersRepo usersRepo;
//
//    @Mock
//    IFollowersRepo followersRepo;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    void testGetPublisher() {
//        UserEntity ue = new UserEntity();
//        ue.setId(1L);
//        ue.setUsername("testUser");
//        ue.setStatus("chyzhyk");
//        ue.setDescription("drank water from a fontaine");
//        PublisherStats ps = new PublisherStats();
//        ps.setUploads(2);
//        ps.setLikes(3);
//        ps.setDislikes(4);
//        ps.setFollowers(5);
//        ps.setComments(6);
//        ue.setStatistics(ps);
//
//        when(usersRepo.findByUsername(anyString())).thenReturn(ue);
//        when(usersRepo.getByUsername(anyString())).thenReturn(Optional.of(() -> 1L));
//        when(followersRepo.existsById(any(FollowerId.class))).thenReturn(true);
//
//        UserDataResponse uds = userService.getPublisher("", "");
//        assertNotNull(uds);
//        assertEquals(ue.getUsername(), uds.getUsername());
//        assertEquals(ue.getStatus(), uds.getStatus());
//        assertEquals(ue.getDescription(), uds.getDescription());
//        assertEquals(ue.getStatistics(), ps);
//    }
}
