package edu.ukma.blog.models.user.mappers;

import edu.ukma.blog.models.user.UserEntity;
import edu.ukma.blog.models.user.responses.SignupResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserEntity_SignupResponse {
    SignupResponse toSignupResponse(UserEntity user);
}
