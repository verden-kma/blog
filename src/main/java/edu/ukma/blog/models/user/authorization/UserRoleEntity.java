package edu.ukma.blog.models.user.authorization;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "user_role")
public class UserRoleEntity {
    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private Integer id;

    @Column(unique = true)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_to_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<UserPermissionEntity> permissions;
}
