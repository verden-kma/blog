package edu.ukma.blog.repositories.projections.user;

public interface FollowerSubscriberView {
    DataPair getId();

    interface DataPair {
        long getSubscriberId();
    }
}
