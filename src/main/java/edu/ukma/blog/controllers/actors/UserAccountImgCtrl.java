package edu.ukma.blog.controllers.actors;

import edu.ukma.blog.constants.ImageConstants;
import edu.ukma.blog.exceptions.server_internal.ServerCriticalError;
import edu.ukma.blog.services.IUserImageService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserAccountImgCtrl {
    private final IUserImageService imageService;

    @PutMapping(value = "/avatar")
    public void setAvatarImage(@RequestPart MultipartFile image,
                               Principal principal) {
        imageService.setAvatar(image, principal.getName());
    }

    @GetMapping(value = "/{username}/avatar", produces = ImageConstants.TARGET_MEDIA_TYPE)
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

    @PutMapping(value = "/top-banner")
    public void setTopBanner(@RequestPart MultipartFile image,
                             Principal principal) {
        imageService.setTopBanner(image, principal.getName());
    }

    @GetMapping(path = "/{username}/top-banner", produces = ImageConstants.TARGET_MEDIA_TYPE)
    public byte[] getTopBanner(@PathVariable String username) {
        Optional<File> banner = imageService.getTopBanner(username);
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
