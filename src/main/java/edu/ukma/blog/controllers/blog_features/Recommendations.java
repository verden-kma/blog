package edu.ukma.blog.controllers.blog_features;

import edu.ukma.blog.models.compositeIDs.RecordId;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@RestController("/recommendations")
public class Recommendations {
    /**
     * user may need recommendations in context or out of context<br>
     * <i>in context</i> - when he is looking at a specific record, we assume user likes it, so
     * every recommended record must be connected to users who liked current record<br>
     * <i>out of context</i> - user just wants to get recommendations, he does not look at any record
     *
     * @param username id of a user for whom a recommendation is to be generated
     * @param recordId id of the current record
     * @param block    ordinal of a chunk of recommendations
     * @return list of <code>RecordId</code>s
     */
    @GetMapping("/evaluations/{username}")
    public List<RecordId> byRecordEvaluations(@PathVariable String username,
                                              @RequestBody(required = false) RecordId recordId,
                                              @RequestPart int block) {
        throw new NotImplementedException();
    }

    @GetMapping("/subscriptions/{username}")
    public List<String> bySubscriptions(@PathVariable String username,
                                        @RequestPart int block) {
        throw new NotImplementedException();
    }
}
