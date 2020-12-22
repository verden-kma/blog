package edu.ukma.blog.repositories.graph_repos.graph_projections;

import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
public interface RecordView {
    long getPublisherId();

    int getRecordOwnId();
}
