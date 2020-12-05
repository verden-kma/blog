package edu.ukma.blog.repositories.projections.user;

public interface PublisherPreviewBaseView {
    long getId();

    String getUsername();

    StatView getStatistics();

    interface StatView {
        int getUploads();

        int getFollowers();
    }
}
