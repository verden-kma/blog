package edu.ukma.blog.controllers.blog_features;

import edu.ukma.blog.PropertyAccessor;
import edu.ukma.blog.SpringApplicationContext;
import edu.ukma.blog.models.record.MinResponseRecord;
import edu.ukma.blog.models.record.RecordEntity_;
import edu.ukma.blog.services.IRecordService;
import edu.ukma.blog.utils.LazyContentPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DigestCtrl {
    private static final int DIGEST_PAGE_SIZE;

    static {
        String beanName = PropertyAccessor.class.getSimpleName();
        String propertyAccessorBeanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
        DIGEST_PAGE_SIZE = ((PropertyAccessor) SpringApplicationContext
                .getBean(propertyAccessorBeanName)).getdigestPageSize();
    }

    @Autowired
    private IRecordService recordService;

    @GetMapping("/digest")
    public LazyContentPage<MinResponseRecord> getDigest(@RequestParam int page) {
        Pageable pageable = PageRequest.of(page, DIGEST_PAGE_SIZE, Sort.by(RecordEntity_.TIMESTAMP).descending());
        return recordService.getMinResponsePage(pageable);
    }
}
