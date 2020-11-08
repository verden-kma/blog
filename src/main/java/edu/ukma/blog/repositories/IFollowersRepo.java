package edu.ukma.blog.repositories;

import edu.ukma.blog.models.Follower;
import edu.ukma.blog.models.compositeIDs.FollowerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFollowersRepo extends JpaRepository<Follower, FollowerId> {
}
