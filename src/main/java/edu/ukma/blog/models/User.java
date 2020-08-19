package edu.ukma.blog.models;

import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue
    private long id;

    private String username;

    private String status;

    private String desc;

    @ElementCollection
    private List<Long> followers;

    @ElementCollection
    private List<Integer> records;
}
