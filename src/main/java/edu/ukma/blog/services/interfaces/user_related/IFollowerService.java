package edu.ukma.blog.services.interfaces.user_related;


import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IFollowerService {
    void addFollower(long publisherId, long followerId);

    List<Long> getFollowersBlock(long publisherId, Pageable pageable);

    void removeFollower(long publisherId, long followerId);
}
