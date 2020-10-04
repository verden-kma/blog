package edu.ukma.blog.services.implementations;

import edu.ukma.blog.constants.ImageConstants;
import edu.ukma.blog.exceptions.ServerCriticalException;
import edu.ukma.blog.exceptions.WrongFileFormatException;
import edu.ukma.blog.repositories.IRecordsRepo;
import edu.ukma.blog.services.IRecordImageService;
import edu.ukma.blog.utils.AlphaNumGenerator;
import edu.ukma.blog.utils.IconHandler;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Iterator;
import java.util.Random;

import static edu.ukma.blog.constants.ImageConstants.*;

@Service
public class RecordImageService implements IRecordImageService {
    // note: maximum number of images that can be stored is ~2^41.
    // It is not enough for 2^63 users, but just fine for debug
    // 3x512 with 8 IMG_ID_LENGTH should be enough
    private static final String PATH_PREFIX = ImageConstants.PATH_PREFIX + "/records";
    private static final File IMAGE_ROOT = new File(PATH_PREFIX);
    private static final String PATH_TEMPLATE = "/%d/%d/";
    private static final long COMPRESSION_THRESHOLD = 512 * 1024;
    private static final int IMG_ID_LENGTH = 8; // the complete id of an image is in form PATH_TEMPLATE/xxxxxxxx
    private static final String ICON_SUFFIX = "-icon." + TARGET_IMAGE_FORMAT;
    private static final String COMPRESSED_SUFFIX = "-min." + TARGET_IMAGE_FORMAT;


    private final Random random = new Random();

    @Autowired
    private IRecordsRepo recordsRepo;

    static {
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
     * @throws ServerCriticalException              - if internal server error occurs
     * @throws WrongFileFormatException - if the <code>original</code> is not an image of acceptable format
     */
    @Override
    public String saveImage(MultipartFile original) throws ServerCriticalException, WrongFileFormatException {
        FormatType imgType = validateFormat(original);

        try {
            String path = String.format(PATH_TEMPLATE, random.nextInt(FOLDERS_WIDTH), random.nextInt(FOLDERS_WIDTH));
            String location;
            do {
                location = path + AlphaNumGenerator.generate(IMG_ID_LENGTH);
            } while (recordsRepo.existsByImgLocation(location));

            BufferedImage originalImg = ImageIO.read(original.getInputStream());
            File originalStored = new File(IMAGE_ROOT, location + "." + TARGET_IMAGE_FORMAT);
            if (imgType == FormatType.TARGET) original.transferTo(originalStored);
            else ImageIO.write(originalImg, TARGET_IMAGE_FORMAT, originalStored);


            if (originalStored.length() > COMPRESSION_THRESHOLD) {
                saveCompressed(originalImg, location);
            }

            IconHandler.saveIcon(originalImg, new File(IMAGE_ROOT, location + ICON_SUFFIX).getPath());

            originalImg.flush();
            System.out.println("location: " + location);
            return location;
        } catch (IOException e) {
            throw new ServerCriticalException(e);
        }
    }

    private void saveCompressed(final BufferedImage image, String originLocation) throws IOException {
        File compressedImageFile = new File(IMAGE_ROOT, originLocation + COMPRESSED_SUFFIX);
        OutputStream os = new FileOutputStream(compressedImageFile);

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(TARGET_IMAGE_FORMAT);
        ImageWriter writer = writers.next();

        ImageOutputStream ios = ImageIO.createImageOutputStream(os);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();

        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(0.7f);  // Change the quality value you prefer
        writer.write(null, new IIOImage(image, null, null), param);

        os.close();
        ios.close();
        writer.dispose();
    }

    /**
     * check that a file passed is of an appropriate format or throw exception otherwise
     *
     * @param origin file to validate
     * @return type of a valid file's format
     * @throws WrongFileFormatException if <code>origin</code> file has unacceptable format
     */
    private static FormatType validateFormat(final MultipartFile origin) throws WrongFileFormatException {
        String fileFormat = FilenameUtils.getExtension(origin.getOriginalFilename());
        if (fileFormat == null) {
            throw new WrongFileFormatException(TARGET_IMAGE_FORMAT, ACCEPTABLE_FORMATS, "null");
        }
        if (fileFormat.equalsIgnoreCase(TARGET_IMAGE_FORMAT)) return FormatType.TARGET;
        for (String acceptableFormat : ACCEPTABLE_FORMATS) {
            if (fileFormat.equalsIgnoreCase(acceptableFormat)) return FormatType.ACCEPTABLE;
        }
        throw new WrongFileFormatException(TARGET_IMAGE_FORMAT, ACCEPTABLE_FORMATS, fileFormat);
    }

    enum FormatType {
        TARGET,
        ACCEPTABLE
    }

    @Override
    public File getImage(String location) {
        return new File(IMAGE_ROOT, location + '.' + TARGET_IMAGE_FORMAT);
    }

    @Override
    public File getImageMin(String location) {
        return new File(IMAGE_ROOT, location + COMPRESSED_SUFFIX);
    }

    @Override
    public File getImageIcon(String location) {
        return new File(IMAGE_ROOT, location + ICON_SUFFIX);
    }
}
