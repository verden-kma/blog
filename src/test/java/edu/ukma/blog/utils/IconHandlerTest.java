package edu.ukma.blog.utils;

import edu.ukma.blog.constants.ImageConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class IconHandlerTest {
    private static final String OUTPUT_IMAGE_LOCATION = "outputTest.".concat(ImageConstants.TARGET_IMAGE_FORMAT);

    @AfterEach
    void clean() {
        new File(OUTPUT_IMAGE_LOCATION).delete();
    }

    @Test
    void testSaveIcon() {
        int iconSize = 300;
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("src/test/resources/test_files/valid-image.jpg"));
            IconHandler.saveIcon(bufferedImage, OUTPUT_IMAGE_LOCATION, iconSize);
            bufferedImage.flush();
        } catch (IOException e) {
            fail(e.getMessage());
        }

        try {
            File imgFile = new File(OUTPUT_IMAGE_LOCATION);
            BufferedImage image = ImageIO.read(imgFile);
            assertTrue(imgFile.exists());
            assertEquals(iconSize, image.getHeight());
            assertTrue(image.getWidth() == image.getHeight());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}
