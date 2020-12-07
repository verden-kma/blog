package edu.ukma.blog.services;

import edu.ukma.blog.models.record.ResponseRecord;
import edu.ukma.blog.models.user.responses.UserDataPreviewResponse;
import edu.ukma.blog.utils.LazyContentPage;
import org.springframework.data.domain.Pageable;

public interface ISearchService {
    LazyContentPage<UserDataPreviewResponse> findPopularPublishers(String prefix, Pageable publisherPageable,
                                                                   long userId, int numPreviewImgs);

    LazyContentPage<ResponseRecord> findRecordsWithTitleLike(String titleSubstr, Pageable pageable, long userId);
}
