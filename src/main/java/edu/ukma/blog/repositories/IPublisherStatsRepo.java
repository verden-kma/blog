package edu.ukma.blog.repositories;

import edu.ukma.blog.models.user.PublisherStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IPublisherStatsRepo extends JpaRepository<PublisherStats, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE PublisherStats ps SET ps.uploads = ps.uploads + 1 WHERE ps.id = :userId")
    void incUploadsCount(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE PublisherStats ps SET ps.followers = ps.followers + 1 WHERE ps.id = :userId")
    void incFollowersCount(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE PublisherStats ps SET ps.followers = ps.followers - 1 WHERE ps.id = :userId")
    void decFollowersCount(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE PublisherStats ps SET ps.likes = ps.likes + 1 WHERE ps.id = :userId")
    void incLikesCount(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE PublisherStats ps SET ps.likes = ps.likes - 1 WHERE ps.id = :userId")
    void decLikesCount(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE PublisherStats ps SET ps.dislikes = ps.dislikes + 1 WHERE ps.id = :userId")
    void incDislikesCount(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE PublisherStats ps SET ps.dislikes = ps.dislikes - 1 WHERE ps.id = :userId")
    void decDislikesCount(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE PublisherStats ps SET ps.comments = ps.comments + 1 WHERE ps.id = :userId")
    void incCommentsCount(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE PublisherStats ps SET ps.comments = ps.comments - 1 WHERE ps.id = :userId")
    void decCommentsCount(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE PublisherStats ps SET ps.uploads = ps.uploads - 1, ps.likes = ps.likes - :numLikes, " +
            "ps.dislikes = ps.dislikes - :numDislikes, ps.comments = ps.comments - :numComm WHERE ps.id = :userId")
    void removeRecordStats(@Param("userId") Long userId, @Param("numLikes") int numLikes,
                           @Param("numDislikes") int numDislikes, @Param("numComm") int numComm);
}
