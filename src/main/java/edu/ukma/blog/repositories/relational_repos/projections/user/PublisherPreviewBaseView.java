package edu.ukma.blog.repositories.relational_repos.projections.user;

public interface PublisherPreviewBaseView {
    long getId();

    String getUsername();

    StatView getStatistics();

    interface StatView {
        int getUploads();

        int getFollowers();
    }
}
