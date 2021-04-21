package edu.ukma.blog.repositories.relational_repos.user_related;

import edu.ukma.blog.models.user.authorization.UserRole;
import edu.ukma.blog.models.user.authorization.UserRoleEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleReadonlyRepo extends org.springframework.data.repository.Repository<UserRoleEntity, Integer> {
    UserRoleEntity findByRole(UserRole role);
}
