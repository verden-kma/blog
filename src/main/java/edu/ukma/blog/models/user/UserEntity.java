package edu.ukma.blog.models.user;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Entity
public class UserEntity {
    @Id
    @GeneratedValue
    private Long id;

//    @NotBlank
//    private String publicId;

    @NotBlank
    private String encryptedPassword;

    @NotBlank
    @Size(min = 3, max = 20)
    @Column(unique = true)
    private String username;

    private String status; // short description

    private String description;

    @ElementCollection
    private List<Long> followers;

    @ElementCollection
    private List<Long> subscriptions;

    @ElementCollection
    private List<Long> records;
}
