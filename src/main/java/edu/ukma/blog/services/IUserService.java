package edu.ukma.blog.services;

import edu.ukma.blog.models.user.RequestUserSignup;
import edu.ukma.blog.models.user.ResponseUser;
import edu.ukma.blog.models.user.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    UserEntity addUser(RequestUserSignup userData);

    ResponseUser getUser(String userId);

    boolean banUser(String userId);

    UserEntity getUserEntity(String username);
}
