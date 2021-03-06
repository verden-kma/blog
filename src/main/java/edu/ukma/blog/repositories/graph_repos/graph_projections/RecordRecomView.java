package edu.ukma.blog.repositories.graph_repos.graph_projections;

import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
public interface RecordRecomView {
    String getUuid();

    long getPublisherId();

    int getRecordOwnId();

    int getStrength();
}
