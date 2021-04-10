package edu.ukma.blog.models.user.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RegistrationRequestEntity {
    @Id
    @Type(type = "uuid-char")
    // seems like the problem with mysql-uuid implementation does not allow it to work with binary
    private UUID token;

    @Column(nullable = false)
    private LocalDateTime expires;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String encryptedPassword;

    private String status;

    private String description;
}
