package edu.ukma.blog.controllers.blog_features;

import com.google.common.collect.BiMap;
import edu.ukma.blog.models.composite_id.RecordId;
import edu.ukma.blog.services.IRecommendService;
import edu.ukma.blog.services.IUserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recommendations")
public class RecommendationsCtrl {
    private static final int PUBLISHERS_RECOM_LIMIT = 10;
    private static final int RECORDS_RECOM_LIMIT = 10;

    private final IRecommendService recommendService;

    private final IUserService userService;

    public RecommendationsCtrl(IRecommendService recommendService, IUserService userService) {
        this.recommendService = recommendService;
        this.userService = userService;
    }

    @GetMapping("/evaluations")
    public List<PublicRecordId> byRecordEvaluations(@RequestBody(required = false) PublicRecordId publicRecordIdId,
                                                    Principal principal) {
        List<RecordId> recomRecs;
        if (publicRecordIdId == null) {
            recomRecs = recommendService.getRecordRecoms(
                    userService.getUserIdByUsername(principal.getName()), RECORDS_RECOM_LIMIT);
        } else {
            long publisherId = userService.getUserIdByUsername(publicRecordIdId.getPublisher());
            recomRecs = recommendService.getRecordRecomsByRecord(
                    new RecordId(publisherId, publicRecordIdId.getRecordOwnId()), RECORDS_RECOM_LIMIT);
        }

        BiMap<Long, String> idsBiMap = userService.getUserIdentifiersBimap(recomRecs.stream()
                .map(RecordId::getPublisherId)
                .collect(Collectors.toList()));
        return recomRecs.stream()
                .map(x -> new PublicRecordId(idsBiMap.get(x.getPublisherId()), x.getRecordOwnId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/subscriptions")
    public List<String> bySubscriptions(Principal principal) {
        List<Long> recomPubl = recommendService.getSubscriptionRecoms(userService.getUserIdByUsername(principal.getName()),
                PUBLISHERS_RECOM_LIMIT);
        BiMap<Long, String> idsBiMap = userService.getUserIdentifiersBimap(recomPubl);
        return recomPubl.stream().map(idsBiMap::get).collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class PublicRecordId {
        private String publisher;
        private int recordOwnId;
    }
}
