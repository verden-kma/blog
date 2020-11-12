package edu.ukma.blog.services;

import com.google.common.collect.BiMap;
import edu.ukma.blog.models.user.UserEntity;
import edu.ukma.blog.models.user.requests.EditUserRequestModel;
import edu.ukma.blog.models.user.requests.UserSignupRequest;
import edu.ukma.blog.models.user.responses.UserPageResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService extends UserDetailsService {
    UserEntity addUser(UserSignupRequest userData);

    long getUserId(String username);

    UserPageResponse getUser(String username);

    void updateUser(String username, EditUserRequestModel update);

    boolean banUser(String username);

    BiMap<Long, String> getUserIdentifiersBimap(List<Long> ids);

    List<String> getUsernames(List<Long> userIds);
}
