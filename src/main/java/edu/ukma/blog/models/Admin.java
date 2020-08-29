package edu.ukma.blog.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Entity
public class Admin {
    @Id
    @GeneratedValue
    private long id;

    @Size(min = 3, max = 15, message = "message must <- [3,15]")
    @NotEmpty
    private String username;
}
