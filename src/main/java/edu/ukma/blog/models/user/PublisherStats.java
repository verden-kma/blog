package edu.ukma.blog.models.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "publisher_stats")
@RequiredArgsConstructor
@NoArgsConstructor
public class PublisherStats {
    @Id
    @Column(name = "publisher_id")
    private long id;

    @NonNull
    @OneToOne
    @MapsId
    @JoinColumn(name = "publisher_id")
    private UserEntity publisher;

    private int uploads;

    private int followers;

    private int likes;

    private int dislikes;

    private int comments;
}
