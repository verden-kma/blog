package edu.ukma.blog.controllers.blog_features;

import edu.ukma.blog.models.compositeIDs.RecordId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@RestController("/recommendations")
public class Recommendations {
    @GetMapping("/evaluations/{username}")
    public List<RecordId> byRecordEvaluations(@PathVariable String username,
                                              @RequestPart int block) {
        throw new NotImplementedException();
    }

    @GetMapping("/subscriptions/{username}")
    public List<String> bySubscriptions(@PathVariable String username,
                                        @RequestPart int block) {
        throw new NotImplementedException();
    }
}
