package edu.ukma.blog.repositories;

import edu.ukma.blog.models.user.UserEntity;
import edu.ukma.blog.repositories.projections.user.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface IUsersRepo extends JpaRepository<UserEntity, Long> {
    boolean existsUserByUsername(String username);

    UserEntity findByUsername(String username);

    Optional<UserIdView> getByUsername(String username);

    StatisticsView getById(long userId);

    List<UserEntityIdsView> findByIdIn(List<Long> ids);

    List<UserNameView> findAllByIdIn(Collection<Long> id);

    boolean deleteByUsername(String username);

    @Query("SELECT user FROM " +
            "UserEntity user INNER JOIN user.statistics stats " +
            "WHERE user.username LIKE CONCAT(:usernamePrefix, '%') " +
            "ORDER BY stats.followers DESC")
    List<PublisherPreviewBaseView> findPopularPublishersWithUsernamePrefix(String usernamePrefix, Pageable pageable);

    int countAllByUsernameStartingWithIgnoreCase(String usernamePrefix);
}
