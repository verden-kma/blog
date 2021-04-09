package edu.ukma.blog.services.implementations;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import edu.ukma.blog.exceptions.user.SignupRequestTimedOut;
import edu.ukma.blog.exceptions.user.UsernameDuplicateException;
import edu.ukma.blog.exceptions.user.UsernameMissingException;
import edu.ukma.blog.exceptions.user.WrongPasswordProvidedException;
import edu.ukma.blog.models.composite_id.FollowerId;
import edu.ukma.blog.models.record.RecordEntity_;
import edu.ukma.blog.models.user.PublisherStats;
import edu.ukma.blog.models.user.RegistrationRequestEntity;
import edu.ukma.blog.models.user.UserEntity;
import edu.ukma.blog.models.user.UserEntity_;
import edu.ukma.blog.models.user.mappers.IRegistrationRequest_User;
import edu.ukma.blog.models.user.mappers.IUserEntity_SignupResponse;
import edu.ukma.blog.models.user.requests.EditUserPasswordRequest;
import edu.ukma.blog.models.user.requests.EditUserRequest;
import edu.ukma.blog.models.user.requests.UserSignupRequest;
import edu.ukma.blog.models.user.responses.SignupResponse;
import edu.ukma.blog.models.user.responses.UserDataPreviewResponse;
import edu.ukma.blog.models.user.responses.UserDataResponse;
import edu.ukma.blog.repositories.IFollowersRepo;
import edu.ukma.blog.repositories.IRecordsRepo;
import edu.ukma.blog.repositories.IRegistrationRequestRepo;
import edu.ukma.blog.repositories.IUsersRepo;
import edu.ukma.blog.repositories.graph_repos.IUserNodesRepo;
import edu.ukma.blog.repositories.projections.user.UserEntityIdsView;
import edu.ukma.blog.repositories.projections.user.UserNameView;
import edu.ukma.blog.services.IEmailService;
import edu.ukma.blog.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    @Value("${signupExpiration}")
    private final long SIGNUP_EXPIRATION;

    @PersistenceContext
    private EntityManager entityManager;

    private final IUsersRepo usersRepo;

    private final PasswordEncoder passwordEncoder;

    private final IFollowersRepo followersRepo;

    private final IRecordsRepo recordsRepo;

    private final IUserNodesRepo userNodesRepo;

    private final IRegistrationRequestRepo signupRepo;

    private final IRegistrationRequest_User registrationRequest_userMapper;

    private final IUserEntity_SignupResponse userEntity_signupResponseMapper;

    private final IEmailService emailService;

    @Override
    @Transactional
    public void createSignUpRequest(UserSignupRequest userData) {
        if (usersRepo.existsUserByUsername(userData.getUsername()))
            throw new UsernameDuplicateException(userData.getUsername());
        RegistrationRequestEntity registrationRequest = RegistrationRequestEntity.builder()
                .token(UUID.randomUUID())
                .expires(LocalDateTime.now().plusSeconds(SIGNUP_EXPIRATION))
                .email(userData.getEmail())
                .username(userData.getUsername())
                .encryptedPassword(passwordEncoder.encode(userData.getPassword()))
                .status(userData.getStatus())
                .description(userData.getDescription())
                .build();

        signupRepo.save(registrationRequest);
        signupRepo.deleteByExpiresBefore(LocalDateTime.now());
        emailService.sendAccountActivation(registrationRequest.getEmail(), registrationRequest.getUsername(),
                registrationRequest.getToken().toString());
    }

    @Override
    public SignupResponse confirmRequest(UUID token) {
        RegistrationRequestEntity registrationRequest = signupRepo.findByTokenAndExpiresAfter(token, LocalDateTime.now())
                .orElseThrow(SignupRequestTimedOut::new);

        UserEntity newUser = registrationRequest_userMapper.registrationRequestToUser(registrationRequest);
        newUser.setStatistics(new PublisherStats(newUser));

        if (usersRepo.existsUserByUsername(newUser.getUsername()))
            throw new UsernameDuplicateException(newUser.getUsername());
        usersRepo.save(newUser);

        return userEntity_signupResponseMapper.userEntityToSignupResponse(newUser);
    }

    @Override
    public long getUserIdByUsername(String username) {
        return usersRepo.getByUsername(username)
                .orElseThrow(() -> new UsernameMissingException(username))
                .getId();
    }

    @Override
    public UserDataResponse getPublisher(String user, String publisher) {
        UserDataResponse respUser = new UserDataResponse();
        UserEntity pUser = usersRepo.findByUsername(publisher);
        respUser.setFollowed(followersRepo.existsById(new FollowerId(getUserIdByUsername(publisher), getUserIdByUsername(user))));
        BeanUtils.copyProperties(pUser, respUser);
        BeanUtils.copyProperties(pUser.getStatistics(), respUser);
        return respUser;
    }

    @Override
    public UserDataPreviewResponse getPublisherPreview(String publisher, String user, int recPrevNum) {
        long publisherId = getUserIdByUsername(publisher);
        long userId = getUserIdByUsername(user);

        UserDataPreviewResponse preview = new UserDataPreviewResponse();
        preview.setPublisher(publisher);
        preview.setFollowed(followersRepo.existsById(new FollowerId(publisherId, userId)));
        Pageable pageable = PageRequest.of(0, recPrevNum, Sort.by(RecordEntity_.TIMESTAMP).descending());
        preview.setLastRecords(recordsRepo.findById_PublisherId(publisherId, pageable)
                .stream()
                .map(x -> x.getId().getRecordOwnId())
                .collect(Collectors.toList()));
        PublisherStats stats = usersRepo.getById(publisherId).getStatistics();
        BeanUtils.copyProperties(stats, preview);
        return preview;
    }

    @Override
    @Transactional
    public void updateUser(String username, EditUserRequest editUser) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<UserEntity> criteriaUpdate = cb.createCriteriaUpdate(UserEntity.class);
        Root<UserEntity> root = criteriaUpdate.from(UserEntity.class);

        if (editUser.getStatus() != null) {
            criteriaUpdate.set(root.get(UserEntity_.status), editUser.getStatus())
                    .where(cb.equal(root.get(UserEntity_.username), username));
        }
        if (editUser.getDescription() != null) {
            criteriaUpdate.set(root.get(UserEntity_.description), editUser.getDescription())
                    .where(cb.equal(root.get(UserEntity_.username), username));
        }

        entityManager.createQuery(criteriaUpdate).executeUpdate();
    }

    @Override
    @Transactional
    public void updateUserPassword(String username, EditUserPasswordRequest editRequest) {
        String validPass = usersRepo.getEnpassByUsername(username).getEncryptedPassword();
        if (!passwordEncoder.matches(editRequest.getCurrentPassword(), validPass))
            throw new WrongPasswordProvidedException("provided password is invalid");

        String offeredNewEnPass = passwordEncoder.encode(editRequest.getNewPassword());
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<UserEntity> criteriaUpdate = cb.createCriteriaUpdate(UserEntity.class);
        Root<UserEntity> root = criteriaUpdate.from(UserEntity.class);
        criteriaUpdate.set(root.get(UserEntity_.encryptedPassword), offeredNewEnPass)
                .where(cb.equal(root.get(UserEntity_.username), username));
        entityManager.createQuery(criteriaUpdate).executeUpdate();
    }

    @Override
    public boolean banUser(String username) {
        usersRepo.getByUsername(username).ifPresent(idView -> userNodesRepo.deleteById(idView.getId()));
        return usersRepo.deleteByUsername(username);
    }

    @Override
    public BiMap<Long, String> getUserIdentifiersBimap(List<Long> ids) {
        return usersRepo.findByIdIn(ids)
                .stream()
                .collect(Collectors.toMap(UserEntityIdsView::getId,
                        UserEntityIdsView::getUsername,
                        (String a, String b) -> b,
                        HashBiMap::create));
    }

    @Override
    public List<String> getUsernamesByIds(List<Long> userIds) {
        return usersRepo.findAllByIdIn(userIds).stream()
                .map(UserNameView::getUsername).collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = usersRepo.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException(username);
        return new User(user.getUsername(), user.getEncryptedPassword(), Collections.emptyList());
    }
}
