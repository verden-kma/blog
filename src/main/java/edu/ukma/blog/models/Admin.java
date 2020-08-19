package edu.ukma.blog.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Admin {
    @Id
    @GeneratedValue
    private long id;

    private String username;
}
