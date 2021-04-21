package edu.ukma.blog.repositories.relational_repos.projections.record;

public interface RecordOwnIdView {
    CmpIdView getId();

    interface CmpIdView {
        int getRecordOwnId();
    }
}
