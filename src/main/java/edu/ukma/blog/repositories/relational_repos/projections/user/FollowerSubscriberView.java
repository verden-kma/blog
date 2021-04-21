package edu.ukma.blog.repositories.relational_repos.projections.user;

public interface FollowerSubscriberView {
    DataPair getId();

    interface DataPair {
        long getSubscriberId();
    }
}
