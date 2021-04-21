package edu.ukma.blog.models.user;

import edu.ukma.blog.models.user.authorization.UserRoleEntity;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "user_entity")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "user_id_gen")
    @TableGenerator(name = "user_id_gen", initialValue = 0, allocationSize = 1)
    @Column(name = "id")
    private long id;

    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    private String encryptedPassword;

    private String status;

    private String description;

    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "role_id", columnDefinition = "int default 1")
    private UserRoleEntity role;

    @OneToOne(mappedBy = "publisher", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private PublisherStats statistics;
}