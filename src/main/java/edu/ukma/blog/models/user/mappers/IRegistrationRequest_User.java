package edu.ukma.blog.models.user.mappers;

import edu.ukma.blog.models.user.RegistrationRequestEntity;
import edu.ukma.blog.models.user.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IRegistrationRequest_User {
    UserEntity registrationRequestToUser(RegistrationRequestEntity requestEntity);
}
