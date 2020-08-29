package edu.ukma.blog.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
//@Table(name = "users")
public class User {
    public User(String id, String username) {
        this.id = id;
        this.username = username;
    }

    @Id
    private String id;

    @NotBlank
    private String username;

    private String status; // short description

    private String description;

    @ElementCollection
    private List<String> followers;

    @ElementCollection
    private List<String> subscriptions;

    @ElementCollection
    private List<Integer> records;
}
