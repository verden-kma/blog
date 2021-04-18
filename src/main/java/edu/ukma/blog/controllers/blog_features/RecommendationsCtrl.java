package edu.ukma.blog.controllers.blog_features;

import com.google.common.collect.BiMap;
import edu.ukma.blog.models.composite_id.RecordId;
import edu.ukma.blog.models.record.ResponseRecord;
import edu.ukma.blog.models.user.responses.UserDataPreviewResponse;
import edu.ukma.blog.services.interfaces.features.IRecommendService;
import edu.ukma.blog.services.interfaces.record_related.IRecordService;
import edu.ukma.blog.services.interfaces.user_related.IUserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationsCtrl {
    @Value("${record-recommendation-size}")
    private final int PUBLISHERS_RECOM_LIMIT;

    @Value("${publisher-recommendation-size}")
    private final int RECORDS_RECOM_LIMIT;

    @Value("${records-preview-block}")
    private final int RECORDS_PREVIEW_BLOCK;

    private final IRecommendService recommendService;

    private final IUserService userService;

    private final IRecordService recordService;

    @GetMapping("/subscriptions")
    public List<UserDataPreviewResponse> bySubscriptions(Principal principal) {
        List<Long> recomPubl = recommendService.getSubscriptionRecoms(userService.getUserIdByUsername(principal.getName()),
                PUBLISHERS_RECOM_LIMIT);
        long userId = userService.getUserIdByUsername(principal.getName());
        return recomPubl.stream()
                .map(publ -> userService.getPublisherPreview(publ, userId, RECORDS_PREVIEW_BLOCK))
                .collect(Collectors.toList());
    }

    @GetMapping("/evaluations")
    public List<ResponseRecord> byEvaluationUserTargeted(Principal principal) {
        List<RecordId> recomRecs = recommendService.getRecordRecoms(userService.getUserIdByUsername(principal.getName()), RECORDS_RECOM_LIMIT);
        return recordService.getResponseByIds(recomRecs, userService.getUserIdByUsername(principal.getName()));
    }

    @GetMapping("/evaluations/{publisher}/{recordId}")
    public List<PublicRecordId> byEvaluationRecordTargeted(@PathVariable String publisher, @PathVariable int recordId, Principal principal) {
        long userId = userService.getUserIdByUsername(principal.getName());
        long publisherId = userService.getUserIdByUsername(publisher);
        List<RecordId> recomRecs = recommendService.getRecordRecomsByRecord(
                new RecordId(publisherId, recordId), userId, RECORDS_RECOM_LIMIT);
        BiMap<Long, String> idsBiMap = userService.getUserIdentifiersBimap(recomRecs.stream()
                .map(RecordId::getPublisherId)
                .collect(Collectors.toList()));
        return recomRecs.stream()
                .map(x -> new PublicRecordId(idsBiMap.get(x.getPublisherId()), x.getRecordOwnId()))
                .collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class PublicRecordId {
        private String publisher;
        private int recordOwnId;
    }
}
