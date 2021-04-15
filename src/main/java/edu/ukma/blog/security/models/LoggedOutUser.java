package edu.ukma.blog.security.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class LoggedOutUser {
    @Id
    private String username;
}
