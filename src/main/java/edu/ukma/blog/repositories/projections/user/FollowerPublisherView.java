package edu.ukma.blog.repositories.projections.user;

public interface FollowerPublisherView {
    FollowerSubscriberView.DataPair getId();

    interface DataPair {
        long getPublisherId();
    }
}
