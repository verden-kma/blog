package edu.ukma.blog.repositories.projections.user;

import edu.ukma.blog.models.user.PublisherStats;

public interface StatisticsView {
    PublisherStats getStatistics();
}
