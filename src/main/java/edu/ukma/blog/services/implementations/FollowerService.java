package edu.ukma.blog.services.implementations;

import edu.ukma.blog.models.Follower;
import edu.ukma.blog.models.compositeIDs.FollowerId;
import edu.ukma.blog.repositories.IFollowersRepo;
import edu.ukma.blog.services.IFollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowerService implements IFollowerService {
    @Autowired
    private IFollowersRepo followersRepo;

    @Override
    public void addFollower(long publisherId, long followerId) {
        followersRepo.save(new Follower(new FollowerId(publisherId, followerId)));
    }

    @Override
    public List<Long> getFollowers(long publisherId) {
        return followersRepo.findAllById_Publisher(publisherId)
                .stream().map(x -> x.getId().getSubscriber()).collect(Collectors.toList());
    }


    @Override
    public List<Long> getFollowersBlock(long publisherId, Pageable pageable) {
        return followersRepo.findAllById_Publisher(publisherId, pageable)
                .stream().map(x -> x.getId().getSubscriber()).collect(Collectors.toList());
    }

    @Override
    public void removeFollower(long publisherId, long followerId) {
        followersRepo.deleteById(new FollowerId(publisherId, followerId));
    }
}
