package edu.ukma.blog.models.user.mappers;

import edu.ukma.blog.models.user.UserEntity;
import edu.ukma.blog.models.user.requests.UserSignupRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ISignupRequest_UserEntity {
    UserEntity toUserEntity(UserSignupRequest signupRequest);
}
