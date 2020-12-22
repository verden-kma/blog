package edu.ukma.blog.repositories.projections.record;

import edu.ukma.blog.models.composite_id.RecordId;

public interface MinRecordView {
    RecordId getId();

    String getCaption();

    String getImgLocation();
}
