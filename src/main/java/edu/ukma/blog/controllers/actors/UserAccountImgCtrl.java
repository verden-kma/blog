package edu.ukma.blog.controllers.actors;

import edu.ukma.blog.constants.ImageConstants;
import edu.ukma.blog.exceptions.ServerCriticalException;
import edu.ukma.blog.exceptions.UsernameMissingException;
import edu.ukma.blog.repositories.IUsersRepo;
import edu.ukma.blog.services.IUserImageService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@RestController
@RequestMapping("/users/{username}")
public class UserAccountImgCtrl {
    @Autowired
    private IUserImageService imageService;

    @Autowired
    private IUsersRepo usersRepo;

    @PutMapping(path = "/avatar") // , consumes = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE}
    public void setAvatarImage(@PathVariable String username,
                               @RequestPart MultipartFile image) {
        long id = usersRepo.getIdByUsername(username).orElseThrow(() -> new UsernameMissingException(username));
        imageService.setAvatar(image, id);
    }

    @GetMapping(value = "/avatar", produces = ImageConstants.TARGET_MEDIA_TYPE)
    public byte[] getAvatarImage(@PathVariable String username) { // if image is present then length of content is > 0
        long id = usersRepo.getIdByUsername(username).orElseThrow(() -> new UsernameMissingException(username));
        Optional<File> icon = imageService.getAvatar(id);
        if (icon.isPresent()) {
            try (InputStream stream = new FileInputStream(icon.get())) {
                return IOUtils.toByteArray(stream);
            } catch (IOException e) {
                throw new ServerCriticalException(e);
            }
        } else {
            return null;
        }
    }

    @DeleteMapping(path = "/avatar")
    public void restoreDefaultAvatar(@PathVariable String username) {
        long id = usersRepo.getIdByUsername(username).orElseThrow(() -> new UsernameMissingException(username));
        imageService.removeAvatar(id);
    }
}
