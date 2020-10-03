package edu.ukma.blog.repositories;

import edu.ukma.blog.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsersRepo extends JpaRepository<UserEntity, Long> {
    boolean existsUserByUsername(String username);

    UserEntity findByUsername(String username);

    boolean deleteByUsername(String username);

    @Query("SELECT user.id FROM UserEntity user WHERE user.username=:username")
    Optional<Long> getIdByUsername(@Param(value = "username") String username);
}
