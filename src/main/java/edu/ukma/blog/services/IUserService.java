package edu.ukma.blog.services;

import com.google.common.collect.BiMap;
import edu.ukma.blog.models.user.RequestUserSignup;
import edu.ukma.blog.models.user.ResponseUser;
import edu.ukma.blog.models.user.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService extends UserDetailsService {
    UserEntity addUser(RequestUserSignup userData);

    long getUserId(String username);

    ResponseUser getUser(String username);

    boolean banUser(String username);

    BiMap<Long, String> getUserIdentifiersBimap(List<Long> ids);

}
