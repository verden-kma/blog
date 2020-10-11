package edu.ukma.blog.services.implementations;

import edu.ukma.blog.exceptions.user.UsernameDuplicateException;
import edu.ukma.blog.exceptions.user.UsernameMissingException;
import edu.ukma.blog.models.user.RequestUserSignup;
import edu.ukma.blog.models.user.ResponseUser;
import edu.ukma.blog.models.user.UserEntity;
import edu.ukma.blog.repositories.IUsersRepo;
import edu.ukma.blog.services.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService implements IUserService {

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
    public boolean banUser(String username) {
        return usersRepo.deleteByUsername(username);
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
