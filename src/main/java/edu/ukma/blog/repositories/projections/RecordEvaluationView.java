package edu.ukma.blog.repositories.projections;

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
