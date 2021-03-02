package edu.ukma.blog.controllers.blog_features;

import edu.ukma.blog.models.record.MinResponseRecord;
import edu.ukma.blog.models.record.RecordEntity_;
import edu.ukma.blog.services.IRecordService;
import edu.ukma.blog.utils.LazyContentPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

@RestController
@RequiredArgsConstructor
public class DigestCtrl {
    @Value("${digestPageSize}")
    private final int DIGEST_PAGE_SIZE;
//            = ((PropertyAccessor) SpringApplicationContext
//            .getBean(PropertyAccessor.PROPERTY_ACCESSOR_BEAN_NAME)).getDigestPageSize();

    private final IRecordService recordService;

    @GetMapping("/digest")
    public LazyContentPage<MinResponseRecord> getDigest(@RequestParam @Min(0) int page) {
        Pageable pageable = PageRequest.of(page, DIGEST_PAGE_SIZE, Sort.by(RecordEntity_.TIMESTAMP).descending());
        return recordService.getMinResponsePage(pageable);
    }
}
