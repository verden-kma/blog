package edu.ukma.blog.services;

import edu.ukma.blog.models.record.ResponseRecord;
import edu.ukma.blog.models.user.responses.PublisherPreview;
import edu.ukma.blog.utils.LazyContentPage;
import org.springframework.data.domain.Pageable;

public interface ISearchService {
    LazyContentPage<PublisherPreview> findPopularPublishers(String prefix, Pageable publisherPageable, long userId, int numPreviewImgs);

    LazyContentPage<ResponseRecord> findRecordsWithTitleLike(String titleSubstr, Pageable pageable, long userId);
}
