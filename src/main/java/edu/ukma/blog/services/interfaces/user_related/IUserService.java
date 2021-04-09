package edu.ukma.blog.services.interfaces.user_related;

import com.google.common.collect.BiMap;
import edu.ukma.blog.models.user.requests.EditUserPasswordRequest;
import edu.ukma.blog.models.user.requests.EditUserRequest;
import edu.ukma.blog.models.user.requests.UserSignupRequest;
import edu.ukma.blog.models.user.responses.SignupResponse;
import edu.ukma.blog.models.user.responses.UserDataPreviewResponse;
import edu.ukma.blog.models.user.responses.UserDataResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

public interface IUserService extends UserDetailsService {
    void assertActive(String username);

    long getUserIdByUsername(String username);

    List<String> getUsernamesByIds(List<Long> userIds);

    BiMap<Long, String> getUserIdentifiersBimap(List<Long> ids);

    void createAdmin(UserSignupRequest signupRequest);

    void createSignUpRequest(UserSignupRequest userData);

    SignupResponse confirmRequest(UUID token);

    UserDataResponse getPublisher(String user, String publisher);

    UserDataPreviewResponse getPublisherPreview(String publisher, String user, int recPrevNum);

    void updateUser(String username, EditUserRequest update);

    void updateUserPassword(String username, EditUserPasswordRequest editRequest);

    void banUser(String username);

    void cancelBan(String username);
}
