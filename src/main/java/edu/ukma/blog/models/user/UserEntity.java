package edu.ukma.blog.models.user;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "user_entity")
public class UserEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @NotBlank
    @Size(min = 3, max = 20)
    @Column(unique = true)
    private String username;

    @NotBlank
    private String encryptedPassword;

    private String status; // short description

    private String description;

    @OneToOne(mappedBy = "publisher", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private PublisherStats statistics;
}