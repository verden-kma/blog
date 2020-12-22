package edu.ukma.blog.repositories;

import edu.ukma.blog.models.composite_id.FollowerId;
import edu.ukma.blog.models.simple_interaction.Follower;
import edu.ukma.blog.repositories.projections.user.FollowerPublisherView;
import edu.ukma.blog.repositories.projections.user.FollowerSubscriberView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface IFollowersRepo extends JpaRepository<Follower, FollowerId> {
    Set<FollowerPublisherView> findById_SubscriberIdAndId_PublisherIdIn(long subscriberId, Collection<Long> publisherId);

    List<FollowerSubscriberView> findAllById_PublisherId(long publisher, Pageable pageable);
}
