package edu.ukma.blog.services.implementations;

import edu.ukma.blog.exceptions.server_internal.WrongFileFormatException;
import edu.ukma.blog.services.implementations.record_related.RecordImageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static edu.ukma.blog.constants.ImageConstants.TARGET_IMAGE_FORMAT;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RecordImageServiceTest {

    private static final String TARGET_SUFFIX = '.' + TARGET_IMAGE_FORMAT;
    private static final String COMPRESSED_SUFFIX = "-min." + TARGET_IMAGE_FORMAT;
    private static final String ICON_SUFFIX = "-icon." + TARGET_IMAGE_FORMAT;
    private final String ROOT = "src/test/resources/test_files/";
    private final RecordImageService recordImageService;
    private String outputPath;

    @Autowired
    RecordImageServiceTest(RecordImageService imageService) throws NoSuchFieldException, IllegalAccessException {
        this.recordImageService = imageService;
        setup();
    }

    private void setup() throws NoSuchFieldException, IllegalAccessException {
        {
            Field pathTemplate = RecordImageService.class.getDeclaredField("PATH_TEMPLATE");
            pathTemplate.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(pathTemplate, pathTemplate.getModifiers() & ~Modifier.FINAL);
            pathTemplate.set(null, "/");
        }

        {
            Field imageRoot = RecordImageService.class.getDeclaredField("IMAGE_ROOT");
            imageRoot.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(imageRoot, imageRoot.getModifiers() & ~Modifier.FINAL);
            imageRoot.set(null, new File("src/test/resources/test_files/"));
        }
    }

    @AfterEach
    void clean() {
        new File(ROOT, outputPath + TARGET_SUFFIX).delete();
        new File(ROOT, outputPath + COMPRESSED_SUFFIX).delete();
        new File(ROOT, outputPath + ICON_SUFFIX).delete();
    }

    @Test
    @DisplayName("saveBigImage")
    void shouldPassIfCreatesTargetMinIconImages() throws IOException {
        MultipartFile image = new MockMultipartFile("test-img.jpg", "rand-size.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(new File(ROOT, "valid-image.jpg")));

        outputPath = recordImageService.saveImage(image);

        File target = new File(ROOT, outputPath + TARGET_SUFFIX);
        File compressed = new File(ROOT, outputPath + COMPRESSED_SUFFIX);
        assertTrue(target.exists());
        assertTrue(compressed.exists());
        assertTrue(target.length() > compressed.length());
        assertTrue(new File(ROOT, outputPath + ICON_SUFFIX).exists());
    }

    @Test
    @DisplayName("saveSmallImage")
    void shouldPassIfNoMinImageIsCreatedForSmallImage() throws IOException {
        final String root = "src/test/resources/test_files/";
        MultipartFile image = new MockMultipartFile("test-img.jpg", "rand-size.jpg",
                MediaType.IMAGE_JPEG_VALUE, new FileInputStream(new File(root, "small-valid-image.png")));

        outputPath = recordImageService.saveImage(image);

        assertTrue(new File(root, outputPath + TARGET_SUFFIX).exists());
        assertFalse(new File(root, outputPath + COMPRESSED_SUFFIX).exists());
        assertTrue(new File(root, outputPath + ICON_SUFFIX).exists());
    }

    @Test
    void saveWrongFormatFile() throws IOException {
        MultipartFile image = new MockMultipartFile("test-img.jpg", "rand-size.jpg",
                MediaType.IMAGE_GIF_VALUE,
                new FileInputStream(new File("src/test/resources/test_files/wrong-image-format.tiff")));

        assertThrows(WrongFileFormatException.class, () -> recordImageService.saveImage(image));
    }
}
