package edu.ukma.blog.repositories;

import edu.ukma.blog.models.user.requests.RegistrationRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IRegistrationRequestRepo extends JpaRepository<RegistrationRequestEntity, UUID> {
    Optional<RegistrationRequestEntity> findByTokenAndExpiresAfter(UUID id, LocalDateTime now);

    //@Modifying
    void deleteByExpiresBefore(LocalDateTime currentTime);
}
