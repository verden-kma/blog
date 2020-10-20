package edu.ukma.blog.services.implementations;

import edu.ukma.blog.constants.ImageConstants;
import edu.ukma.blog.exceptions.server_internal.ServerCriticalError;
import edu.ukma.blog.exceptions.server_internal.WrongFileFormatException;
import edu.ukma.blog.services.IUserImageService;
import edu.ukma.blog.utils.IconHandler;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;

import static edu.ukma.blog.constants.ImageConstants.*;
import static edu.ukma.blog.constants.ImageConstants.ACCEPTABLE_FORMATS;

@Service
public class UserImageService implements IUserImageService {
    private static final String PATH_PREFIX = ImageConstants.PATH_PREFIX + "/account";
    private static final File IMAGE_ROOT = new File(PATH_PREFIX);
//    private static final String PATH_TEMPLATE = "/%d/%d/";
    private static final String AVATAR_SUFFIX = "-ava." + ImageConstants.TARGET_IMAGE_FORMAT;
    private static final String BANNER_SUFFIX = "-bnr." + ImageConstants.TARGET_IMAGE_FORMAT;
    private final Random random = new Random();

    static {
        if (!IMAGE_ROOT.exists()) {
            IMAGE_ROOT.mkdir(); // todo: maintain a true R-way-like file system so that it is possible to find a folder by username
//            for (int i = 0; i < ImageConstants.FOLDERS_WIDTH; i++)
//                for (int j = 0; j < ImageConstants.FOLDERS_WIDTH; j++)
//                    new File(IMAGE_ROOT, String.format(PATH_TEMPLATE, i, j)).mkdirs();
        }
    }

    @Override
    public Optional<File> getAvatar(long userId) {
        File maybeImage = new File(IMAGE_ROOT, userId + AVATAR_SUFFIX);
        if (!maybeImage.exists()) return Optional.empty();
        return maybeImage.exists() ? Optional.of(maybeImage) : Optional.empty();
    }

    @Override
    public void setAvatar(MultipartFile multipartImage, long userId) {
        try {
            IconHandler.saveIcon(ImageIO.read(multipartImage.getInputStream()),
                    new File(IMAGE_ROOT, userId + AVATAR_SUFFIX).getPath());
        } catch (IOException e) {
            throw new ServerCriticalError(e);
        }
    }

    @Override
    public void removeAvatar(long userId) {
        File maybeImage = new File(IMAGE_ROOT, userId + AVATAR_SUFFIX);
        maybeImage.delete();
    }

    @Override
    public Optional<File> getTopBanner(long userId) {
        File maybeImage = new File(IMAGE_ROOT, userId + BANNER_SUFFIX);
        if (!maybeImage.exists()) return Optional.empty();
        return maybeImage.exists() ? Optional.of(maybeImage) : Optional.empty();
    }

    @Override // todo: take username as a param and use it as a prefix for file name
    public void setTopBanner(MultipartFile imageFile, long userId) {
        if (imageFile.isEmpty() || imageFile.getContentType() == null
                || !ACCEPTABLE_MEDIA_TYPES.contains(imageFile.getContentType()))
            throw new WrongFileFormatException(TARGET_IMAGE_FORMAT, ACCEPTABLE_FORMATS, imageFile.getContentType());

//        String path = String.format(PATH_TEMPLATE, random.nextInt(FOLDERS_WIDTH), random.nextInt(FOLDERS_WIDTH));
        File locationOnDisk = new File(IMAGE_ROOT, userId + BANNER_SUFFIX);
        try {
            BufferedImage bImg = ImageIO.read(imageFile.getInputStream()); // todo: stink of a cheaper way to get dims
            if (bImg.getWidth() == topBannerDims.getWidth() && bImg.getHeight() == topBannerDims.getHeight()) {
                imageFile.transferTo(locationOnDisk);
            } else {
                int width = bImg.getWidth();
                int height = bImg.getHeight();
                double inputRatio = (double) width / height;
                double targetRatio = (double) topBannerDims.getWidth() / topBannerDims.getHeight();
                final int centerX = width / 2;
                final int centerY = height / 2;
                if (Math.abs(targetRatio - inputRatio) > 1e-5) {
                    if (targetRatio > inputRatio) height = (int) (width / targetRatio);
                    else width = (int) (height * targetRatio);
                    BufferedImage cropped = Scalr.crop(bImg, centerX - width / 2, centerY - height / 2, width, height);
                    bImg.flush();
                    bImg = cropped;
                }
                Scalr.resize(bImg, Scalr.Mode.FIT_EXACT, topBannerDims.getWidth(), topBannerDims.getHeight());
                ImageIO.write(bImg, TARGET_IMAGE_FORMAT, locationOnDisk);
                bImg.flush();
                System.out.println("banner location: " + locationOnDisk.getPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeTopBanner(long userId) {
        File maybeImage = new File(IMAGE_ROOT, userId + BANNER_SUFFIX);
        maybeImage.delete();
    }
}
