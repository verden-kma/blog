package edu.ukma.blog.controllers.actors;

import edu.ukma.blog.constants.ImageConstants;
import edu.ukma.blog.exceptions.server_internal.ServerCriticalError;
import edu.ukma.blog.services.IUserImageService;
import edu.ukma.blog.services.IUserService;
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
    private IUserService userService;

    @PutMapping(path = "/avatar") // , consumes = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE}
    public void setAvatarImage(@PathVariable String username,
                               @RequestPart MultipartFile image) {
        imageService.setAvatar(image, userService.getUserId(username));
    }

    @GetMapping(value = "/avatar", produces = ImageConstants.TARGET_MEDIA_TYPE)
    public byte[] getAvatarImage(@PathVariable String username) { // if image is present then length of content is > 0
        Optional<File> icon = imageService.getAvatar(userService.getUserId(username));
        if (icon.isPresent()) {
            try (InputStream stream = new FileInputStream(icon.get())) {
                return IOUtils.toByteArray(stream);
            } catch (IOException e) {
                throw new ServerCriticalError(e);
            }
        } else {
            return null; // null avatar means default image icon
        }
    }

    @DeleteMapping(path = "/avatar")
    public void restoreDefaultAvatar(@PathVariable String username) {
        imageService.removeAvatar(userService.getUserId(username));
    }

    @PostMapping(path = "/top-banner")
    public void setTopBanner(@PathVariable String username,
                             @RequestPart MultipartFile image) {
        imageService.setTopBanner(image, userService.getUserId(username));
    }

    @GetMapping(path = "/top-banner", produces = ImageConstants.TARGET_MEDIA_TYPE)
    public byte[] getTopBanner(@PathVariable String username) {
        Optional<File> banner = imageService.getTopBanner(userService.getUserId(username));
        if (banner.isPresent()) {
            try (InputStream stream = new FileInputStream(banner.get())) {
                return IOUtils.toByteArray(stream);
            }catch (IOException e) {
                throw new ServerCriticalError(e);
            }
        } else {
            return null;
        }
    }

    @DeleteMapping(path = "/top-banner")
    public void removeTopBanner(@PathVariable String username) {
        imageService.removeTopBanner(userService.getUserId(username));
    }
}
