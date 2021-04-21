package edu.ukma.blog.repositories.relational_repos.projections.user;

public interface FollowerPublisherView {
    DataPair getId();

    interface DataPair {
        long getPublisherId();
    }
}
