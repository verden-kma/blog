package edu.ukma.blog.services.implementations.user_related;

import edu.ukma.blog.constants.ImageConstants;
import edu.ukma.blog.exceptions.server_internal.ServerCriticalError;
import edu.ukma.blog.exceptions.server_internal.WrongFileFormatException;
import edu.ukma.blog.services.interfaces.user_related.IUserImageService;
import edu.ukma.blog.utils.IconHandler;
import edu.ukma.blog.utils.VarLenRWayTree;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static edu.ukma.blog.constants.ImageConstants.*;

@Service
@RequiredArgsConstructor
public class UserImageService implements IUserImageService, InitializingBean {
    private static final String PATH_PREFIX = ImageConstants.PATH_PREFIX + "/account";
    private static final File IMAGE_ROOT = new File(PATH_PREFIX);
    @Value("${min-username-len}")
    private final int MIN_USERNAME_LEN;
    @Value("#{T(edu.ukma.blog.services.implementations.user_related.UserImageService).buildPath(${minUsernameLen})}")
    private final String PATH_TEMPLATE;
    private static final String AVATAR_SUFFIX = "-ava." + ImageConstants.TARGET_IMAGE_FORMAT;
    private static final String BANNER_SUFFIX = "-bnr." + ImageConstants.TARGET_IMAGE_FORMAT;

    public static String buildPath(final int MIN_USERNAME_LEN) {
        StringBuilder pathTemplateBuilder = new StringBuilder(3 * MIN_USERNAME_LEN);
        for (int i = 0; i < MIN_USERNAME_LEN; i++)
            pathTemplateBuilder.append("/%c");
        return pathTemplateBuilder.append('/').toString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!IMAGE_ROOT.exists()) {
            IMAGE_ROOT.mkdir();
            VarLenRWayTree.build(MIN_USERNAME_LEN, PATH_TEMPLATE, IMAGE_ROOT);
        }
    }

    private String buildPathByUsername(String username) {
        Character[] pathArgs = new Character[MIN_USERNAME_LEN];
        for (int i = 0; i < MIN_USERNAME_LEN; i++)
            pathArgs[i] = username.charAt(i);
        String tail = username.length() > MIN_USERNAME_LEN ? username.substring(MIN_USERNAME_LEN) : "";
        return String.format(PATH_TEMPLATE, (Object[]) pathArgs).concat(tail);
    }

    @Override
    public Optional<File> getAvatar(String username) {
        File maybeImage = new File(IMAGE_ROOT, buildPathByUsername(username) + AVATAR_SUFFIX);
        if (!maybeImage.exists()) return Optional.empty();
        return maybeImage.exists() ? Optional.of(maybeImage) : Optional.empty();
    }

    @Override
    public void setAvatar(MultipartFile multipartImage, String username) {
        try {
            IconHandler.saveIcon(ImageIO.read(multipartImage.getInputStream()),
                    new File(IMAGE_ROOT, buildPathByUsername(username) + AVATAR_SUFFIX).getPath(), AVATAR_SIZE);
        } catch (IOException e) {
            throw new ServerCriticalError(e);
        }
    }

    @Override
    public void removeAvatar(String username) {
        File maybeImage = new File(IMAGE_ROOT, buildPathByUsername(username) + AVATAR_SUFFIX);
        maybeImage.delete();
    }

    @Override
    public Optional<File> getTopBanner(String username) {
        File maybeImage = new File(IMAGE_ROOT, buildPathByUsername(username) + BANNER_SUFFIX);
        if (!maybeImage.exists()) return Optional.empty();
        return maybeImage.exists() ? Optional.of(maybeImage) : Optional.empty();
    }

    @Override
    public void setTopBanner(MultipartFile imageFile, String username) {
        if (imageFile.isEmpty() || imageFile.getContentType() == null
                || !ACCEPTABLE_MEDIA_TYPES.contains(imageFile.getContentType()))
            throw new WrongFileFormatException(TARGET_IMAGE_FORMAT, ACCEPTABLE_FORMATS, imageFile.getContentType());

        File locationOnDisk = new File(IMAGE_ROOT, buildPathByUsername(username) + BANNER_SUFFIX);
        try {
            BufferedImage bImg = ImageIO.read(imageFile.getInputStream());
            if (bImg.getWidth() == TOP_BANNER_DIMS.getWidth() && bImg.getHeight() == TOP_BANNER_DIMS.getHeight()) {
                imageFile.transferTo(locationOnDisk);
            } else {
                int width = bImg.getWidth();
                int height = bImg.getHeight();
                double inputRatio = (double) width / height;
                double targetRatio = (double) TOP_BANNER_DIMS.getWidth() / TOP_BANNER_DIMS.getHeight();
                final int centerX = width / 2;
                final int centerY = height / 2;
                if (Math.abs(targetRatio - inputRatio) > 1e-5) {
                    if (targetRatio > inputRatio) height = (int) (width / targetRatio);
                    else width = (int) (height * targetRatio);
                    BufferedImage cropped = Scalr.crop(bImg, centerX - width / 2, centerY - height / 2, width, height);
                    bImg.flush();
                    bImg = cropped;
                }
                BufferedImage toFlush = Scalr.resize(bImg, Scalr.Mode.FIT_EXACT,
                        TOP_BANNER_DIMS.getWidth(), TOP_BANNER_DIMS.getHeight());
                ImageIO.write(toFlush, TARGET_IMAGE_FORMAT, locationOnDisk);
                bImg.flush();
                toFlush.flush();
                System.out.println("banner location: " + locationOnDisk.getPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeTopBanner(String username) {
        File maybeImage = new File(IMAGE_ROOT, buildPathByUsername(username) + BANNER_SUFFIX);
        maybeImage.delete();
    }


}
