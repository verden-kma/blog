package edu.ukma.blog.security.services;

public interface IBlacklistTokenService {
    void removeInvalid(String username);

    boolean checkIsValid(String username);

    void setIsInvalid(String username);
}
