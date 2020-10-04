package edu.ukma.blog.utils;

import edu.ukma.blog.constants.ImageConstants;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class IconHandler {
    public static void saveIcon(final BufferedImage original, String location) throws IOException {
        System.out.println("icon saved to: " + location);
        int minDimension = Math.min(original.getHeight(), original.getWidth());
        int xStart = (original.getWidth() - minDimension) / 2;
        int yStart = (original.getHeight() - minDimension) / 2;

        BufferedImage squareIcon = Scalr.crop(original, xStart, yStart, minDimension, minDimension);
        BufferedImage icon = Scalr.resize(squareIcon, Scalr.Mode.FIT_EXACT, ImageConstants.ICON_SIZE);
        ImageIO.write(icon, ImageConstants.TARGET_IMAGE_FORMAT, new File(location));
        squareIcon.flush();
        icon.flush();
    }
}
