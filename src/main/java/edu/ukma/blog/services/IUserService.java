package edu.ukma.blog.services;

import com.google.common.collect.BiMap;
import edu.ukma.blog.models.user.UserEntity;
import edu.ukma.blog.models.user.requests.EditUserRequestModel;
import edu.ukma.blog.models.user.requests.UserSignupRequest;
import edu.ukma.blog.models.user.responses.PublisherPreview;
import edu.ukma.blog.models.user.responses.UserPageResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService extends UserDetailsService {
    UserEntity addUser(UserSignupRequest userData);

    UserPageResponse getPublisher(String user, String publisher);

    PublisherPreview getPublisherPreview(String publisher, String user, int recPrevNum);

    void updateUser(String username, EditUserRequestModel update);

    boolean banUser(String username);

    long getUserId(String username);

    List<String> getUsernames(List<Long> userIds);

    BiMap<Long, String> getUserIdentifiersBimap(List<Long> ids);
}
