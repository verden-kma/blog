package edu.ukma.blog.repositories;

import edu.ukma.blog.models.user.UserEntity;
import edu.ukma.blog.repositories.projections.user.IdView;
import edu.ukma.blog.repositories.projections.user.PublisherPreviewBaseView;
import edu.ukma.blog.repositories.projections.user.UserEntityIdsView;
import edu.ukma.blog.repositories.projections.user.UserNameView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    List<UserNameView> findAllByIdIn(Collection<Long> id);

    boolean deleteByUsername(String username);

    Optional<IdView> getByUsername(String username);

    List<UserEntityIdsView> findByIdIn(List<Long> ids);

    @Query("SELECT user FROM " +
            "UserEntity user INNER JOIN user.statistics stats " +
            "WHERE user.username LIKE CONCAT(:usernamePrefix, '%') " +
            "ORDER BY stats.followers DESC")
    Slice<PublisherPreviewBaseView> findPopularPublishersWithUsernamePrefix(String usernamePrefix, Pageable pageable);

}
