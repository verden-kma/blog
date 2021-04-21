package edu.ukma.blog.repositories.relational_repos.user_related;

import edu.ukma.blog.models.user.UserEntity;
import edu.ukma.blog.repositories.relational_repos.projections.user.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface IUsersRepo extends JpaRepository<UserEntity, Long> {
    boolean existsUserByUsername(String username);

    Optional<UserEntity> findByUsername(String username);

    @Query("SELECT user FROM UserEntity user WHERE user.username = :username")
    PasswordView getEnpassByUsername(@Param("username") String username);

    Optional<UserIdView> getByUsername(String username);

    StatisticsView getById(long userId);

    List<UserEntityIdsView> findByIdIn(List<Long> ids);

    List<UserNameView> findAllByIdIn(Collection<Long> id);

//    @Modifying
//        // temporarily unused, was required for user self deletion
//    boolean deleteByUsername(String username);

    @Query("SELECT user FROM " +
            "UserEntity user INNER JOIN user.statistics stats " +
            "WHERE user.username LIKE CONCAT(:usernamePrefix, '%') " +
            "ORDER BY stats.followers DESC")
    List<PublisherPreviewBaseView> findPopularPublishersWithUsernamePrefix(@Param("usernamePrefix") String usernamePrefix, Pageable pageable);

    int countAllByUsernameStartingWithIgnoreCase(String usernamePrefix);

    @Query("SELECT user.isActive FROM UserEntity user WHERE user.username=:username")
    Optional<Boolean> getActiveStatus(@Param("username") String username);

    @Modifying
    @Query("UPDATE UserEntity user SET user.isActive=:isActive WHERE user.username=:username")
    void setActive(@Param("username") String username, @Param("isActive") boolean isActive);
}
