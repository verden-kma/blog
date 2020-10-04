package edu.ukma.blog.services.implementations;

import edu.ukma.blog.constants.ImageConstants;
import edu.ukma.blog.exceptions.ServerCriticalException;
import edu.ukma.blog.services.IUserImageService;
import edu.ukma.blog.utils.IconHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sun.security.x509.AVA;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class UserImageService implements IUserImageService {
    private static final String PATH_PREFIX = ImageConstants.PATH_PREFIX + "/account";
    private static final File IMAGE_ROOT = new File(PATH_PREFIX);
//    private static final String PATH_TEMPLATE = "/%d/%d/";
    private static final String AVATAR_SUFFIX = "-ava." + ImageConstants.TARGET_IMAGE_FORMAT;
    private static final String BACKGROUND_SUFFIX = "-bgr." + ImageConstants.TARGET_IMAGE_FORMAT;

    static {
        if (!IMAGE_ROOT.exists()) {
            IMAGE_ROOT.mkdir();
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
            throw new ServerCriticalException(e);
        }
    }

    @Override
    public void removeAvatar(long userId) {
        File maybeImage = new File(IMAGE_ROOT, userId + AVATAR_SUFFIX);
        maybeImage.delete();
    }

    @Override
    public Optional<File> getMainPageImage(long userId) {
        File maybeImage = new File(IMAGE_ROOT, userId + BACKGROUND_SUFFIX);
        if (!maybeImage.exists()) return Optional.empty();
        return maybeImage.exists() ? Optional.of(maybeImage) : Optional.empty();
    }

    @Override
    public void setMainPageImage(MultipartFile image, long userId) {
        throw new NotImplementedException();
    }

    @Override
    public void removeMainPageImage(long userId) {
        File maybeImage = new File(IMAGE_ROOT, userId + BACKGROUND_SUFFIX);
        maybeImage.delete();
    }
}
