package edu.ukma.blog.services.interfaces.user_related;


import edu.ukma.blog.models.user.responses.UserDataPreviewResponse;
import edu.ukma.blog.utils.EagerContentPage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IFollowerService {
    void addFollower(long publisherId, long followerId);

    List<Long> getFollowersBlock(long publisherId, Pageable pageable);

    EagerContentPage<UserDataPreviewResponse> toEagerFollowersPage(long publisherId,
                                                                   List<UserDataPreviewResponse> content, int pageSize);

    List<Long> getSubscriptionsBlock(long subscriberId, Pageable pageable);

    EagerContentPage<UserDataPreviewResponse> toEagerSubscriptionsPage(long subscriberId,
                                                                       List<UserDataPreviewResponse> content, int pageSize);

    void removeFollower(long publisherId, long followerId);
}
