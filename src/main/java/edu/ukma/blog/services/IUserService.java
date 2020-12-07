package edu.ukma.blog.services;

import com.google.common.collect.BiMap;
import edu.ukma.blog.models.user.requests.EditUserRequest;
import edu.ukma.blog.models.user.requests.UserSignupRequest;
import edu.ukma.blog.models.user.responses.UserDataPreviewResponse;
import edu.ukma.blog.models.user.responses.UserDataResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService extends UserDetailsService {
    long getUserIdByUsername(String username);

    List<String> getUsernamesByIds(List<Long> userIds);

    BiMap<Long, String> getUserIdentifiersBimap(List<Long> ids);

    void addUser(UserSignupRequest userData);

    UserDataResponse getPublisher(String user, String publisher);

    UserDataPreviewResponse getPublisherPreview(String publisher, String user, int recPrevNum);

    void updateUser(String username, EditUserRequest update);

    boolean banUser(String username);
}
