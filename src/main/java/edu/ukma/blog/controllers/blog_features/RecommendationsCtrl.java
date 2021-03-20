package edu.ukma.blog.controllers.blog_features;

import com.google.common.collect.BiMap;
import edu.ukma.blog.models.composite_id.RecordId;
import edu.ukma.blog.models.user.responses.UserDataPreviewResponse;
import edu.ukma.blog.services.IRecommendService;
import edu.ukma.blog.services.IUserService;
import edu.ukma.blog.utils.LazyContentPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationsCtrl {
    @Value("${recordRecommendationSize}")
    private final int PUBLISHERS_RECOM_LIMIT;

    @Value("${publisherRecommendationSize}")
    private final int RECORDS_RECOM_LIMIT;

    @Value("${recordsPreviewBlock}")
    private final int RECORDS_PREVIEW_BLOCK;

    private final IRecommendService recommendService;

    private final IUserService userService;

    @GetMapping("/subscriptions")
    public LazyContentPage<UserDataPreviewResponse> bySubscriptions(Principal principal) {
        List<Long> recomPubl = recommendService.getSubscriptionRecoms(userService.getUserIdByUsername(principal.getName()),
                PUBLISHERS_RECOM_LIMIT);
        BiMap<Long, String> idsBiMap = userService.getUserIdentifiersBimap(recomPubl);
        List<UserDataPreviewResponse> content = recomPubl.stream()
                .map(publ -> userService.getPublisherPreview(idsBiMap.get(publ), principal.getName(), RECORDS_PREVIEW_BLOCK))
                .collect(Collectors.toList());
        return new LazyContentPage<>(content, true);
    }

    @GetMapping("/evaluations")
    public LazyContentPage<PublicRecordId> byRecordEvaluations(@RequestBody(required = false) PublicRecordId publicRecordId,
                                                               Principal principal) {
        List<RecordId> recomRecs;
        if (publicRecordId == null) {
            recomRecs = recommendService.getRecordRecoms(
                    userService.getUserIdByUsername(principal.getName()), RECORDS_RECOM_LIMIT);
        } else {
            long publisherId = userService.getUserIdByUsername(publicRecordId.getPublisher());
            recomRecs = recommendService.getRecordRecomsByRecord(
                    new RecordId(publisherId, publicRecordId.getRecordOwnId()), RECORDS_RECOM_LIMIT);
        }

        BiMap<Long, String> idsBiMap = userService.getUserIdentifiersBimap(recomRecs.stream()
                .map(RecordId::getPublisherId)
                .collect(Collectors.toList()));
        List<PublicRecordId> content = recomRecs.stream()
                .map(x -> new PublicRecordId(idsBiMap.get(x.getPublisherId()), x.getRecordOwnId()))
                .collect(Collectors.toList());
        return new LazyContentPage<>(content, true);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class PublicRecordId {
        private String publisher;
        private int recordOwnId;
    }
}
