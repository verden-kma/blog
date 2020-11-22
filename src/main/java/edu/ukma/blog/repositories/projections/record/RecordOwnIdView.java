package edu.ukma.blog.repositories.projections.record;

public interface RecordOwnIdView {
    CmpIdView getId();

    interface CmpIdView {
        int getRecordOwnId();
    }
}
