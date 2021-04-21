package edu.ukma.blog.controllers.blog_features;

import edu.ukma.blog.models.record.RecordEntity_;
import edu.ukma.blog.models.record.ResponseRecord;
import edu.ukma.blog.models.user.responses.UserDataPreviewResponse;
import edu.ukma.blog.services.interfaces.features.ISearchService;
import edu.ukma.blog.services.interfaces.user_related.IUserService;
import edu.ukma.blog.utils.EagerContentPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchCtrl {
    @Value("${search-page-size}")
    private final int SEARCH_PAGE_SIZE;

    @Value("${records-preview-block}")
    private final int RECORDS_PREVIEW_BLOCK_SIZE;

    private final ISearchService searchService;

    private final IUserService userService;

    @GetMapping("/publishers")
    public EagerContentPage<UserDataPreviewResponse> findPublishers(@RequestParam @NotEmpty String name,
                                                                    @RequestParam @Min(0) int page,
                                                                    Principal principal) {
        long userId = userService.getUserIdByUsername(principal.getName());
        Pageable pageable = PageRequest.of(page, SEARCH_PAGE_SIZE);
        return searchService.findPublishersWithPrefix(name, pageable, userId, RECORDS_PREVIEW_BLOCK_SIZE);
    }

    @GetMapping("/records")
    public EagerContentPage<ResponseRecord> findRecords(@RequestParam @NotEmpty String title,
                                                        @RequestParam @Min(0) int page,
                                                        Principal principal) {
        long userId = userService.getUserIdByUsername(principal.getName());
        Pageable pageable = PageRequest.of(page, SEARCH_PAGE_SIZE, Sort.by(RecordEntity_.TIMESTAMP));
        return searchService.findRecordsWithTitleLike(title, pageable, userId);
    }
}
