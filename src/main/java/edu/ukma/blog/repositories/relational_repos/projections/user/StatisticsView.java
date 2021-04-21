package edu.ukma.blog.repositories.relational_repos.projections.user;

import edu.ukma.blog.models.user.PublisherStats;

public interface StatisticsView {
    PublisherStats getStatistics();
}
