package edu.ukma.blog.controllers.communication;

import edu.ukma.blog.exceptions.user.SelfFollowerException;
import edu.ukma.blog.models.user.responses.UserDataPreviewResponse;
import edu.ukma.blog.services.interfaces.user_related.IFollowerService;
import edu.ukma.blog.services.interfaces.user_related.IUserService;
import edu.ukma.blog.utils.EagerContentPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{username}")
@RequiredArgsConstructor
public class FollowerCtrl {
    @Value("${followers-per-block}")
    private final int FOLLOWERS_BLOCK_SIZE;

    @Value("${subscriptions-per-block}")
    private final int SUBSCRIPTIONS_BLOCK_SIZE;

    @Value("${records-preview-block}")
    private final int RECORDS_PREVIEW_SIZE;

    private final IFollowerService followerService;

    private final IUserService userService;

    @GetMapping("/followers")
    public EagerContentPage<UserDataPreviewResponse> getFollowers(@PathVariable @NotEmpty String username,
                                                                  @RequestParam int page) {
        userService.assertActive(username);
        long publisherId = userService.getUserIdByUsername(username);
        Pageable pageable = PageRequest.of(page, FOLLOWERS_BLOCK_SIZE);
        List<UserDataPreviewResponse> content = followerService.getFollowersBlock(publisherId, pageable)
                .stream().map(user -> userService.getPublisherPreview(user, publisherId, RECORDS_PREVIEW_SIZE))
                .collect(Collectors.toList());
        return followerService.toEagerFollowersPage(publisherId, content, RECORDS_PREVIEW_SIZE);
    }

    @GetMapping("/subscriptions")
    public EagerContentPage<UserDataPreviewResponse> getSubscriptions(@PathVariable @NotEmpty String username,
                                                                      @RequestParam int page) {
        userService.assertActive(username);
        long subscriberId = userService.getUserIdByUsername(username);
        Pageable pageable = PageRequest.of(page, SUBSCRIPTIONS_BLOCK_SIZE);
        List<UserDataPreviewResponse> content = followerService.getSubscriptionsBlock(subscriberId, pageable)
                .stream().map(publisher -> userService.getPublisherPreview(publisher, subscriberId, RECORDS_PREVIEW_SIZE))
                .collect(Collectors.toList());
        return followerService.toEagerSubscriptionsPage(subscriberId, content, RECORDS_PREVIEW_SIZE);
    }

    // used (in target view, in publisher page view, publisher preview)
    @PreAuthorize("hasAuthority('FOLLOW')")
    @PutMapping("/followers")
    public void follow(@PathVariable @NotEmpty String username,
                       Principal principal) {
        userService.assertActive(username);
        if (username.equals(principal.getName())) throw new SelfFollowerException();
        long publisherId = userService.getUserIdByUsername(username);
        long subscriberId = userService.getUserIdByUsername(principal.getName());
        followerService.addFollower(publisherId, subscriberId);
    }

    @PreAuthorize("hasAuthority('FOLLOW')")
    @DeleteMapping("/followers")
    public void unfollow(@PathVariable @NotEmpty String username,
                         Principal principal) {
        long publisherId = userService.getUserIdByUsername(username);
        long subscriberId = userService.getUserIdByUsername(principal.getName());
        followerService.removeFollower(publisherId, subscriberId);
    }
}
