package edu.ukma.blog.controllers.blog_features;

import edu.ukma.blog.PropertyAccessor;
import edu.ukma.blog.SpringApplicationContext;
import edu.ukma.blog.models.record.RecordEntity_;
import edu.ukma.blog.models.record.ResponseRecord;
import edu.ukma.blog.models.user.responses.PublisherPreview;
import edu.ukma.blog.services.ISearchService;
import edu.ukma.blog.services.IUserService;
import edu.ukma.blog.utils.LazyContentPage;
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
public class SearchCtrl {
    private static final int SEARCH_PAGE_SIZE;

    private static final int RECORDS_PREVIEW_BLOCK_SIZE;

    static {
        final PropertyAccessor pa = ((PropertyAccessor) SpringApplicationContext
                .getBean(PropertyAccessor.PROPERTY_ACCESSOR_BEAN_NAME));
        SEARCH_PAGE_SIZE = pa.getSearchPageSize();
        RECORDS_PREVIEW_BLOCK_SIZE = pa.getRecordsPreviewBlock();
    }

    private final ISearchService searchService;

    private final IUserService userService;

    public SearchCtrl(ISearchService searchService, IUserService userService) {
        this.searchService = searchService;
        this.userService = userService;
    }

    // feature-idea: add prioritizing options
//    private static final Map<String, String> PRIORITIZING_METHOD = Collections.unmodifiableMap(
//            new HashMap<String, String>() {{put("most recent", RecordEntity_.TIMESTAMP);}});

    @GetMapping("/publishers")
    public LazyContentPage<PublisherPreview> findPublishers(@RequestParam @NotEmpty String name,
                                                            @RequestParam @Min(0) int page,
                                                            Principal principal) {
        long userId = userService.getUserId(principal.getName());
        Pageable pageable = PageRequest.of(page, SEARCH_PAGE_SIZE);
        return searchService.findPopularPublishers(name, pageable, userId, RECORDS_PREVIEW_BLOCK_SIZE);
    }

    //feature-idea: add record_statistics entity to avoid querying stats every time
    @GetMapping("/records")
    public LazyContentPage<ResponseRecord> findRecords(@RequestParam @NotEmpty String title,
                                                       @RequestParam @Min(0) int page,
                                                       Principal principal) {
        long userId = userService.getUserId(principal.getName());
        Pageable pageable = PageRequest.of(page, SEARCH_PAGE_SIZE, Sort.by(RecordEntity_.TIMESTAMP));
        return searchService.findRecordsWithTitleLike(title, pageable, userId);
    }
}
