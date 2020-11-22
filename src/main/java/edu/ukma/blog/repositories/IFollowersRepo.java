package edu.ukma.blog.repositories;

import edu.ukma.blog.models.Follower;
import edu.ukma.blog.models.compositeIDs.FollowerId;
import edu.ukma.blog.repositories.projections.user.FollowerSubscriberView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFollowersRepo extends JpaRepository<Follower, FollowerId> {
    List<FollowerSubscriberView> findAllById_Publisher(long publisher);

    List<FollowerSubscriberView> findAllById_Publisher(long publisher, Pageable pageable);
}
