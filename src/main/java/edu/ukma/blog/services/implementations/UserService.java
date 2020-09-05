package edu.ukma.blog.services.implementations;

import edu.ukma.blog.models.user.RequestUserSignup;
import edu.ukma.blog.models.user.ResponseUser;
import edu.ukma.blog.models.user.UserEntity;
import edu.ukma.blog.repositories.UsersRepo;
import edu.ukma.blog.services.IUserService;
import edu.ukma.blog.utils.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements IUserService {
    private static final int PUBLIC_PASSWORD_LENGTH = 15;
    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserEntity addUser(RequestUserSignup userData) {
        if (usersRepo.existsUserByUsername(userData.getUsername()))
            throw new RuntimeException("username duplicated");
        UserEntity newUser = new UserEntity();
        BeanUtils.copyProperties(userData, newUser);
        newUser.setPublicId(PasswordUtil.generate(PUBLIC_PASSWORD_LENGTH));
        newUser.setEncryptedPassword(passwordEncoder.encode(userData.getPassword()));
        return usersRepo.save(newUser);
    }

    @Override
    public ResponseUser getUser(Long userId) {
        return null;
    }

    @Override
    public boolean banUser(Long userId) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = usersRepo.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException(username);
        return new User(user.getUsername(), user.getEncryptedPassword(), Collections.emptyList());
    }
}
