package edu.ukma.blog.repositories.relational_repos.projections.record;

public interface RecordEvaluationView {
    Boolean getIsLiker();

    RecordIdView getId();

    interface RecordIdView {
        RecordOwnIdView getRecordId();

        interface RecordOwnIdView {
            int getRecordOwnId();
        }
    }
}
