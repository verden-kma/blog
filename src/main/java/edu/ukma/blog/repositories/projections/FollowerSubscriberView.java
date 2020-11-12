package edu.ukma.blog.repositories.projections;

public interface FollowerSubscriberView {
    DataPair getId();

    interface DataPair {
        long getSubscriber();
    }
}
