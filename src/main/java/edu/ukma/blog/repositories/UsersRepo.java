package edu.ukma.blog.repositories;

import edu.ukma.blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepo extends JpaRepository<User, Long> {
}
