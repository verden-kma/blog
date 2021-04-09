package edu.ukma.blog.models.user.authorization;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "user_permission")
public class UserPermissionEntity {
    @Id
    @GeneratedValue
    @Column(name = "permission_id")
    private Integer id;

    @Column(unique = true)
    @Enumerated(value = EnumType.STRING)
    private UserPermission permission;

}
