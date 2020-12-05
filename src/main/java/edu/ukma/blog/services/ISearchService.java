package edu.ukma.blog.services;

import edu.ukma.blog.models.record.ResponseRecord;
import edu.ukma.blog.models.user.responses.PublisherPreview;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISearchService {
    List<PublisherPreview> findPopularPublishers(String prefix, Pageable publisherPageable, int numPreviewImgs);

    List<ResponseRecord> findRecords(String substr, Pageable pageable);
}
