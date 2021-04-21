package edu.ukma.blog.services.implementations.record_related;

import edu.ukma.blog.constants.ImageConstants;
import edu.ukma.blog.exceptions.server_internal.ServerCriticalError;
import edu.ukma.blog.exceptions.server_internal.WrongFileFormatException;
import edu.ukma.blog.repositories.relational_repos.record_related.IRecordsRepo;
import edu.ukma.blog.services.interfaces.record_related.IRecordImageService;
import edu.ukma.blog.utils.AlphaNumGenerator;
import edu.ukma.blog.utils.IconHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Random;

import static edu.ukma.blog.constants.ImageConstants.*;

@Service
@RequiredArgsConstructor
public class RecordImageService implements IRecordImageService, InitializingBean {
    // note: maximum number of images that can be stored is ~2^41.
    // It is not enough for 2^63 users, but just fine for debug
    // 3x512 with 8 IMG_ID_LENGTH should be enough
    private static final File IMAGE_ROOT = new File(ImageConstants.PATH_PREFIX + "/records");
    @Value("#{T(edu.ukma.blog.services.implementations.record_related.RecordImageService).buildPath(${recordsDirectoriesDepth})}")
    private final String PATH_TEMPLATE;
    @Value("${recordsDirectoriesDepth}")
    private final int DIR_DEPTH;
    @Value("${compression-threshold}")
    private final long COMPRESSION_THRESHOLD;
    private static final int IMG_ID_LENGTH = 8; // the complete id of an image is in form PATH_TEMPLATE/xxxxxxxx
    private static final String TARGET_SUFFIX = '.' + TARGET_IMAGE_FORMAT;
    private static final String COMPRESSED_SUFFIX = "-min." + TARGET_IMAGE_FORMAT;
    private static final String ICON_SUFFIX = "-icon." + TARGET_IMAGE_FORMAT;

    private final Random random = new Random();
    private final IRecordsRepo recordsRepo;

    public static String buildPath(final int DIR_DEPTH) {
        StringBuilder pathBuilder = new StringBuilder(DIR_DEPTH * 3 + 1);
        for (int i = 0; i < DIR_DEPTH; i++) {
            pathBuilder.append("/%d");
        }
        pathBuilder.append('/');
        return pathBuilder.toString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // initialize similar to R-way-tree structure of folders for faster access to files
        // use random to assign images to folders
        if (!IMAGE_ROOT.exists()) {
            IMAGE_ROOT.mkdir();
            for (int i = 0; i < FOLDERS_WIDTH; i++)
                for (int j = 0; j < FOLDERS_WIDTH; j++)
                    new File(IMAGE_ROOT, String.format(PATH_TEMPLATE, i, j)).mkdirs();
        }
    }

    /**
     * stores passed image, its compressed version and icon
     *
     * @param original - file to be saved as an image
     * @return location of the original image in jpg format (distinct part of the path to the image)
     * @throws ServerCriticalError      - if internal server error occurs
     * @throws WrongFileFormatException - if the <code>original</code> is not an image of acceptable format
     */
    @Override
    public String saveImage(MultipartFile original) throws ServerCriticalError, WrongFileFormatException {
        if (original.isEmpty() || original.getContentType() == null
                || !ACCEPTABLE_MEDIA_TYPES.contains(original.getContentType()))
            throw new WrongFileFormatException(TARGET_IMAGE_FORMAT, ACCEPTABLE_FORMATS, original.getContentType());

        String path = String.format(PATH_TEMPLATE, random.nextInt(FOLDERS_WIDTH), random.nextInt(FOLDERS_WIDTH));
        String location;
        do {
            location = path + AlphaNumGenerator.generate(IMG_ID_LENGTH);
        } while (recordsRepo.existsByImgLocation(location));

        try {
            BufferedImage originalImg = ImageIO.read(original.getInputStream());
            File originalStored = new File(IMAGE_ROOT, location + TARGET_SUFFIX);
            if (original.getContentType().equals(TARGET_MEDIA_TYPE)) original.transferTo(originalStored);
            else ImageIO.write(originalImg, TARGET_IMAGE_FORMAT, originalStored);

            if (originalStored.length() > COMPRESSION_THRESHOLD) {
                saveCompressed(originalImg, location);
            }

            IconHandler.saveIcon(originalImg, new File(IMAGE_ROOT, location + ICON_SUFFIX).getPath(), RECORD_ICON_SIZE);

            originalImg.flush();
            System.out.println("location: " + location);
            return location;
        } catch (IOException e) {
            throw new ServerCriticalError(e);
        }
    }

    private void saveCompressed(final BufferedImage image, String originLocation) throws IOException {
        File compressedImageFile = new File(IMAGE_ROOT, originLocation + COMPRESSED_SUFFIX);
        ImageWriter writer = null;
        try (OutputStream os = new FileOutputStream(compressedImageFile);
             ImageOutputStream ios = ImageIO.createImageOutputStream(os)) {

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(TARGET_IMAGE_FORMAT);
            writer = writers.next();
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.7f);  // Change the quality value you prefer
            writer.write(null, new IIOImage(image, null, null), param);
        } finally {
            if (writer != null) writer.dispose();
        }
    }

    @Override
    public File getImage(String location) {
        return new File(IMAGE_ROOT, location + TARGET_SUFFIX);
    }

    @Override
    public File getImageMin(String location) {
        return new File(IMAGE_ROOT, location + COMPRESSED_SUFFIX);
    }

    @Override
    public File getImageIcon(String location) {
        return new File(IMAGE_ROOT, location + ICON_SUFFIX);
    }

    @Override
    @SneakyThrows
    public void deleteImage(String location) {
        Files.delete(Paths.get(IMAGE_ROOT.getPath(), location + TARGET_SUFFIX));
        Files.deleteIfExists(Paths.get(IMAGE_ROOT.getPath(), location + COMPRESSED_SUFFIX));
        Files.delete(Paths.get(IMAGE_ROOT.getPath(), location + ICON_SUFFIX));
    }
}