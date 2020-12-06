package edu.ukma.blog.controllers.actors;

import edu.ukma.blog.constants.ImageConstants;
import edu.ukma.blog.exceptions.server_internal.ServerCriticalError;
import edu.ukma.blog.services.IUserImageService;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/users/{username}")
public class UserAccountImgCtrl {
    private final IUserImageService imageService;

    public UserAccountImgCtrl(IUserImageService imageService) {
        this.imageService = imageService;
    }

    @PutMapping("/avatar") // , consumes = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE}
    public void setAvatarImage(@RequestPart MultipartFile image,
                               Principal principal) {
        imageService.setAvatar(image, principal.getName());
    }

    @GetMapping(value = "/avatar", produces = ImageConstants.TARGET_MEDIA_TYPE)
    public byte[] getAvatarImage(@PathVariable String username) { // if image is present then length of content is > 0
        Optional<File> icon = imageService.getAvatar(username);
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

    @DeleteMapping("/avatar")
    public void restoreDefaultAvatar(Principal principal) {
        imageService.removeAvatar(principal.getName());
    }

    @PutMapping("/top-banner")
    public void setTopBanner(@PathVariable String username,
                             @RequestPart MultipartFile image) {
        imageService.setTopBanner(image, username);
    }

    @GetMapping(path = "/top-banner", produces = ImageConstants.TARGET_MEDIA_TYPE)
    public byte[] getTopBanner(Principal principal) {
        Optional<File> banner = imageService.getTopBanner(principal.getName());
        if (banner.isPresent()) {
            try (InputStream stream = new FileInputStream(banner.get())) {
                return IOUtils.toByteArray(stream);
            } catch (IOException e) {
                throw new ServerCriticalError(e);
            }
        } else {
            return null;
        }
    }

    @DeleteMapping("/top-banner")
    public void removeTopBanner(Principal principal) {
        imageService.removeTopBanner(principal.getName());
    }
}
