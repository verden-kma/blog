package edu.ukma.blog.controllers.blog_features;

import com.google.common.collect.BiMap;
import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.services.IRecommendService;
import edu.ukma.blog.services.IUserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recommendations")
public class RecommendationsCtrl {
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

    private final IRecommendService recommendService;

    private final IUserService userService;

    public RecommendationsCtrl(IRecommendService recommendService, IUserService userService) {
        this.recommendService = recommendService;
        this.userService = userService;
    }

    @GetMapping("/evaluations/{username}")
    public List<PublicRecordId> byRecordEvaluations(@PathVariable String username,
//                                              @RequestBody(required = false) RecordId recordId,
                                                    @RequestParam int block) {
        List<RecordId> recomRecs = recommendService.getRecordRecommendations(userService.getUserIdByUsername(username),
                PageRequest.of(block, 10));
        BiMap<Long, String> idsBiMap = userService.getUserIdentifiersBimap(recomRecs.stream()
                .map(RecordId::getPublisherId)
                .collect(Collectors.toList()));
        return recomRecs.stream()
                .map(x -> new PublicRecordId(idsBiMap.get(x.getPublisherId()), x.getRecordOwnId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/subscriptions/{username}")
    public List<String> bySubscriptions(@PathVariable String username,
                                        @RequestParam int block) {
        List<Long> recomPubl = recommendService.getSubscriptionRecommendations(userService.getUserIdByUsername(username),
                PageRequest.of(block, 10));
        BiMap<Long, String> idsBiMap = userService.getUserIdentifiersBimap(recomPubl);
        return recomPubl.stream().map(idsBiMap::get).collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    static class PublicRecordId {
        private String publisher;
        private int recordOwnId;
    }
}
