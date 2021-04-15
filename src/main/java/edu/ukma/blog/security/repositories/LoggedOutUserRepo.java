package edu.ukma.blog.security.repositories;

import edu.ukma.blog.security.models.LoggedOutUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggedOutUserRepo extends JpaRepository<LoggedOutUser, String> {
}
