package edu.ukma.blog.repositories;

import edu.ukma.blog.models.user.UserEntity;
import edu.ukma.blog.repositories.projections.user.StatisticsView;
import edu.ukma.blog.repositories.projections.user.UserEntityIdsView;
import edu.ukma.blog.repositories.projections.user.UserNameView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface IUsersRepo extends JpaRepository<UserEntity, Long> {
    boolean existsUserByUsername(String username);

    UserEntity findByUsername(String username);

    StatisticsView getByUsername(String username);

    List<UserNameView> findAllByIdIn(Collection<Long> id);

    boolean deleteByUsername(String username);

    @Query("SELECT user.id FROM UserEntity user WHERE user.username=:username")
    Optional<Long> getIdByUsername(@Param(value = "username") String username);

    @Query("SELECT user.username as username, user.id as id FROM UserEntity user WHERE user.id IN (:idList)")
    List<UserEntityIdsView> getUsernamesByIds(@Param(value = "idList") List<Long> idList);
}
