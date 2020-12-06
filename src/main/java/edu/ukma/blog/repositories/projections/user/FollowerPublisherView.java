package edu.ukma.blog.repositories.projections.user;

public interface FollowerPublisherView {
    DataPair getId();

    interface DataPair {
        long getPublisherId();
    }
}
