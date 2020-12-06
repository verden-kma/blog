package edu.ukma.blog.services.implementations;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import edu.ukma.blog.exceptions.user.UsernameDuplicateException;
import edu.ukma.blog.exceptions.user.UsernameMissingException;
import edu.ukma.blog.models.compositeIDs.FollowerId;
import edu.ukma.blog.models.record.RecordEntity_;
import edu.ukma.blog.models.user.PublisherStats;
import edu.ukma.blog.models.user.UserEntity;
import edu.ukma.blog.models.user.UserEntity_;
import edu.ukma.blog.models.user.requests.EditUserRequestModel;
import edu.ukma.blog.models.user.requests.UserSignupRequest;
import edu.ukma.blog.models.user.responses.PublisherPreview;
import edu.ukma.blog.models.user.responses.UserPageResponse;
import edu.ukma.blog.repositories.IFollowersRepo;
import edu.ukma.blog.repositories.IUsersRepo;
import edu.ukma.blog.repositories.projections.user.UserEntityIdsView;
import edu.ukma.blog.repositories.projections.user.UserNameView;
import edu.ukma.blog.services.IRecordService;
import edu.ukma.blog.services.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private IUsersRepo usersRepo;

    @Autowired
    private IRecordService recordService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private IFollowersRepo followersRepo;

    @Override
    public UserEntity addUser(UserSignupRequest userData) {
        if (usersRepo.existsUserByUsername(userData.getUsername()))
            throw new UsernameDuplicateException(userData.getUsername());
        UserEntity newUser = new UserEntity();
        BeanUtils.copyProperties(userData, newUser);
        newUser.setEncryptedPassword(passwordEncoder.encode(userData.getPassword()));
        newUser.setStatistics(new PublisherStats(newUser));
        return usersRepo.save(newUser);
    }

    @Override
    public long getUserId(String username) {
        Optional<Long> maybePublisherId = usersRepo.getIdByUsername(username);
        return maybePublisherId.orElseThrow(() -> new UsernameMissingException(username));
    }

    @Override
    public UserPageResponse getPublisher(String user, String publisher) {
        UserPageResponse respUser = new UserPageResponse();
        UserEntity pUser = usersRepo.findByUsername(user);
        respUser.setFollowed(followersRepo.existsById(new FollowerId(getUserId(user), getUserId(publisher))));
        BeanUtils.copyProperties(pUser, respUser);
        BeanUtils.copyProperties(pUser.getStatistics(), respUser);
        return respUser;
    }

    @Override
    public PublisherPreview getPublisherPreview(String publisher, String user, int recPrevNum) {
        long userId = getUserId(user);
        long publisherId = getUserId(publisher);

        PublisherPreview preview = new PublisherPreview();
        preview.setPublisherName(publisher);
        preview.setFollowed(followersRepo.existsById(new FollowerId(publisherId, userId)));
        Pageable pageable = PageRequest.of(0, recPrevNum, Sort.by(RecordEntity_.TIMESTAMP).descending());
        preview.setLastRecordsImgPaths(recordService.getUserRecordsImgPaths(publisherId, pageable));
        PublisherStats stats = getUserEntity(user).getStatistics();
        BeanUtils.copyProperties(stats, preview);
        return preview;
    }

    @Override
    @Transactional
    public void updateUser(String username, EditUserRequestModel editUser) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<UserEntity> criteriaUpdate = cb.createCriteriaUpdate(UserEntity.class);
        Root<UserEntity> root = criteriaUpdate.from(UserEntity.class);

        if (editUser.getPassword() != null) {
            criteriaUpdate.set(root.get(UserEntity_.encryptedPassword), passwordEncoder.encode(editUser.getPassword()))
                    .where(cb.equal(root.get(UserEntity_.username), username));
        }
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
    public boolean banUser(String username) {
        return usersRepo.deleteByUsername(username);
    }

    @Override
    public BiMap<Long, String> getUserIdentifiersBimap(List<Long> ids) {
        return usersRepo.getUsernamesByIds(ids)
                .stream()
                .collect(Collectors.toMap(UserEntityIdsView::getId,
                        UserEntityIdsView::getUsername,
                        (String a, String b) -> b,
                        HashBiMap::create));
    }

    @Override
    public List<String> getUsernames(List<Long> userIds) {
        return usersRepo.findAllByIdIn(userIds).stream()
                .map(UserNameView::getUsername).collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = getUserEntity(username);
        return new User(user.getUsername(), user.getEncryptedPassword(), Collections.emptyList());
    }

    public UserEntity getUserEntity(String username) {
        UserEntity user = usersRepo.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException(username);
        return user;
    }
}
