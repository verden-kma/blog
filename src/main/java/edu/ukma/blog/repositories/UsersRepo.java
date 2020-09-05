package edu.ukma.blog.repositories;

import edu.ukma.blog.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepo extends JpaRepository<UserEntity, Long> {
    boolean existsUserByUsername(String username);

    UserEntity findByUsername(String username);
}
