package edu.ukma.blog.services.implementations;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import edu.ukma.blog.exceptions.user.UsernameDuplicateException;
import edu.ukma.blog.exceptions.user.UsernameMissingException;
import edu.ukma.blog.models.user.*;
import edu.ukma.blog.repositories.IUsersRepo;
import edu.ukma.blog.repositories.projections.UserEntityIdsView;
import edu.ukma.blog.services.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserEntity addUser(RequestUserSignup userData) {
        if (usersRepo.existsUserByUsername(userData.getUsername()))
            throw new UsernameDuplicateException(userData.getUsername());
        UserEntity newUser = new UserEntity();
        BeanUtils.copyProperties(userData, newUser);
        newUser.setEncryptedPassword(passwordEncoder.encode(userData.getPassword()));
        return usersRepo.save(newUser);
    }

    @Override
    public long getUserId(String username) {
        Optional<Long> maybePublisherId = usersRepo.getIdByUsername(username);
        return maybePublisherId.orElseThrow(() -> new UsernameMissingException(username));
    }

    @Override
    public ResponseUser getUser(String username) {
        ResponseUser respUser = new ResponseUser();
        BeanUtils.copyProperties(usersRepo.findByUsername(username), respUser);
        return respUser;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = usersRepo.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException(username);
        return new User(user.getUsername(), user.getEncryptedPassword(), Collections.emptyList());
    }

    public UserEntity getUserEntity(String username) {
        UserEntity user = usersRepo.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException(username);
        return user;
    }
}
