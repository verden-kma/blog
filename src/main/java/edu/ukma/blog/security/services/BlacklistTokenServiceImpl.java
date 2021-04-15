package edu.ukma.blog.security.services;

import edu.ukma.blog.security.models.LoggedOutUser;
import edu.ukma.blog.security.repositories.LoggedOutUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlacklistTokenServiceImpl implements IBlacklistTokenService {
    private final LoggedOutUserRepo loggedOutUserRepo;

    @Override
    public void removeInvalid(String username) {
        if (loggedOutUserRepo.existsById(username))
            loggedOutUserRepo.deleteById(username);
    }

    @Override
    public boolean checkIsValid(String username) {
        return !loggedOutUserRepo.existsById(username);
    }

    @Override
    public void setIsInvalid(String username) {
        loggedOutUserRepo.save(new LoggedOutUser(username));
    }
}
