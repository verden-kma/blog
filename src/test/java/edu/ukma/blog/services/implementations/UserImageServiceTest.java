package edu.ukma.blog.services.implementations;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

// fimxe: mama I am still a criminal

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserImageServiceTest {

//    static final String username = "Japanese_Dancer";
//    private String outputImageLocation;
//    private final UserImageService userImageService;
//
//    @Autowired
//    UserImageServiceTest(UserImageService userImageService) throws NoSuchFieldException, IllegalAccessException {
//        this.userImageService = userImageService;
//        setUp();
//    }
//
//    void setUp() throws IllegalAccessException, NoSuchFieldException {
//        final Field bannerSuffix = UserImageService.class.getDeclaredField("BANNER_SUFFIX");
//        bannerSuffix.setAccessible(true);
//        outputImageLocation = username + bannerSuffix.get(null);
//
//        {
//            final String pathPrefix = "src/test/resources/test_files/";
//            Field pathRoot = UserImageService.class.getDeclaredField("IMAGE_ROOT");
//            pathRoot.setAccessible(true);
//            Field modifiers = Field.class.getDeclaredField("modifiers");
//            modifiers.setAccessible(true);
//            modifiers.setInt(pathRoot, pathRoot.getModifiers() & ~Modifier.FINAL);
//            pathRoot.set(null, new File(pathPrefix));
//            outputImageLocation = pathPrefix + outputImageLocation;
//        }
//
//        {
//            Field minUsernameLen = UserImageService.class.getDeclaredField("MIN_USERNAME_LEN");
//            minUsernameLen.setAccessible(true);
//            Field modifiers = Field.class.getDeclaredField("modifiers");
//            modifiers.setAccessible(true);
//            modifiers.set(minUsernameLen, minUsernameLen.getModifiers() & ~Modifier.FINAL);
//            minUsernameLen.setInt(null, 0);
//        }
//
//        {
//            Field pathTemplate = UserImageService.class.getDeclaredField("PATH_TEMPLATE");
//            pathTemplate.setAccessible(true);
//            Field modifiers = Field.class.getDeclaredField("modifiers");
//            modifiers.setAccessible(true);
//            modifiers.set(pathTemplate, pathTemplate.getModifiers() & ~Modifier.FINAL);
//            pathTemplate.set(null, "");
//        }
//    }
//
//    @AfterEach
//    @Disabled
//    void clean() {
//        new File(outputImageLocation).delete();
//    }
//
//    @Test
//    void testSetTopBannerWrongSize() throws IOException {
//        MultipartFile mpFile = new MockMultipartFile("test-img.jpg", "rand-size.jpg",
//                MediaType.IMAGE_JPEG_VALUE,
//                new FileInputStream(new File("src/test/resources/test_files/valid-image.jpg")));
//
//        userImageService.setTopBanner(mpFile, username);
//
//        File imgFile = new File(outputImageLocation);
//        assertTrue(imgFile.exists());
//        BufferedImage image = ImageIO.read(imgFile);
//        assertEquals(ImageConstants.TOP_BANNER_DIMS.getWidth(), image.getWidth());
//        assertEquals(ImageConstants.TOP_BANNER_DIMS.getHeight(), image.getHeight());
//        image.flush();
//    }
//
//    @Test
//    void testSetTopBannerExactSize() throws IOException {
//        File inputFile = new File("src/test/resources/test_files/exact-size-banner.jpg");
//        MultipartFile mpFile = new MockMultipartFile("test-img.jpg", "exact-size.jpg",
//                MediaType.IMAGE_JPEG_VALUE, new FileInputStream(inputFile));
//
//        userImageService.setTopBanner(mpFile, username);
//
//        File outputFile = new File(outputImageLocation);
//        assertTrue(outputFile.exists());
//        assertTrue(FileUtils.contentEquals(inputFile, outputFile));
//    }
//
//    @Test
//    void testWrongFileFormat() throws IOException {
//        MultipartFile mpFile = new MockMultipartFile("test-img.gif", "whatever-size.jpg",
//                MediaType.IMAGE_GIF_VALUE,
//                new FileInputStream(new File("src/test/resources/test_files/wrong-image-format.tiff")));
//
//        assertThrows(WrongFileFormatException.class, () -> userImageService.setTopBanner(mpFile, username));
//    }
}
