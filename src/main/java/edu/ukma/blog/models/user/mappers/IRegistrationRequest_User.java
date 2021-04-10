package edu.ukma.blog.models.user.mappers;

import edu.ukma.blog.models.user.UserEntity;
import edu.ukma.blog.models.user.requests.RegistrationRequestEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IRegistrationRequest_User {
    UserEntity registrationRequestToUser(RegistrationRequestEntity requestEntity);
}
