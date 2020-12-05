package edu.ukma.blog.controllers.blog_features;

import edu.ukma.blog.models.record.ResponseRecord;
import edu.ukma.blog.models.user.responses.PublisherPreview;
import edu.ukma.blog.services.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchCtrl {

    @Autowired
    private ISearchService searchService;

    // todo: get size of page prom property
    @GetMapping("/publishers")
    public List<PublisherPreview> findPublishers(@RequestParam String name,
                                                 @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return searchService.findPopularPublishers(name, pageable, 3);
    }

    @GetMapping("/records")
    public List<ResponseRecord> findRecords(@RequestParam String title,
                                            @RequestParam int page) {
        throw new NotImplementedException();
    }
}
