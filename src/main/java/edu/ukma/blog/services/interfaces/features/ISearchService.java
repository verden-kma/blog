package edu.ukma.blog.services.interfaces.features;

import edu.ukma.blog.models.record.ResponseRecord;
import edu.ukma.blog.models.user.responses.UserDataPreviewResponse;
import edu.ukma.blog.utils.EagerContentPage;
import org.springframework.data.domain.Pageable;

public interface ISearchService {
    EagerContentPage<UserDataPreviewResponse> findPublishersWithPrefix(String prefix, Pageable publisherPageable,
                                                                       long userId, int numPreviewImgs);

    EagerContentPage<ResponseRecord> findRecordsWithTitleLike(String titleSubstr, Pageable pageable, long userId);
}
