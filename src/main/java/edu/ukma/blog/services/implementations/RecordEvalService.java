package edu.ukma.blog.services.implementations;

import edu.ukma.blog.models.compositeIDs.EvaluatorId;
import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.models.record.evaluation.Evaluation_;
import edu.ukma.blog.models.simple_interaction.Evaluation;
import edu.ukma.blog.repositories.IEvaluatorsRepo;
import edu.ukma.blog.repositories.IPublisherStatsRepo;
import edu.ukma.blog.repositories.IUsersRepo;
import edu.ukma.blog.repositories.projections.user.UserEntityIdsView;
import edu.ukma.blog.services.IRecordEvalService;
import edu.ukma.blog.utils.LazyContentPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecordEvalService implements IRecordEvalService {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private IEvaluatorsRepo evaluatorsRepo;

    @Autowired
    private IUsersRepo usersRepo;

    @Autowired
    private IPublisherStatsRepo publisherStatsRepo;

    @Override
    public Boolean getReaction(RecordId recordId, long userId) {
        Optional<Evaluation> maybeEvaluation = evaluatorsRepo.findById(new EvaluatorId(recordId, userId));
        return maybeEvaluation.map(Evaluation::getIsLiker).orElse(null);
    }

    @Override
    @Transactional
    public void putEvaluation(RecordId recordId, long userId, boolean eval) {
        Boolean react = getReaction(recordId, userId);
        if (react == null) {
            evaluatorsRepo.save(new Evaluation(new EvaluatorId(recordId, userId), eval));
        } else if (react != eval) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaUpdate<Evaluation> criteriaUpdate = cb.createCriteriaUpdate(Evaluation.class);
            Root<Evaluation> root = criteriaUpdate.from(Evaluation.class);
            criteriaUpdate.set(root.get(Evaluation_.IS_LIKER), eval)
                    .where(cb.equal(root.get(Evaluation_.ID), new EvaluatorId(recordId, userId)));
            em.createQuery(criteriaUpdate).executeUpdate();

            if (react) publisherStatsRepo.decLikesCount(recordId.getPublisherId());
            else publisherStatsRepo.decDislikesCount(recordId.getPublisherId());
        } else return;
        if (eval) publisherStatsRepo.incLikesCount(recordId.getPublisherId());
        else publisherStatsRepo.incDislikesCount(recordId.getPublisherId());
    }

    @Override
    @Transactional
    public void removeEvaluation(RecordId recordId, long userId, boolean eval) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<Evaluation> criteriaDelete = cb.createCriteriaDelete(Evaluation.class);
        Root<Evaluation> root = criteriaDelete.from(Evaluation.class);
        criteriaDelete.where(cb.equal(root.get(Evaluation_.ID), new EvaluatorId(recordId, userId)));
        criteriaDelete.where(cb.equal(root.get(Evaluation_.IS_LIKER), eval));
        boolean hasDeleted = em.createQuery(criteriaDelete).executeUpdate() == 1; // if there is a bug and > 1 deleted

        if (hasDeleted)
            if (eval) publisherStatsRepo.decLikesCount(recordId.getPublisherId());
            else publisherStatsRepo.decDislikesCount(recordId.getPublisherId());
    }

    @Override
    public LazyContentPage<String> getLikers(RecordId recordId, Pageable pageable) {
        return getEvalPageOfKind(recordId, true, pageable);
    }

    @Override
    public LazyContentPage<String> getDislikers(RecordId recordId, Pageable pageable) {
        return getEvalPageOfKind(recordId, false, pageable);
    }

    private LazyContentPage<String> getEvalPageOfKind(RecordId recordId, boolean isLiker, Pageable pageable) {
        Slice<Evaluation> evals = evaluatorsRepo.findAllById_RecordIdAndIsLiker(recordId, isLiker, pageable);

        List<String> evaluators = usersRepo.findByIdIn(evals
                .getContent()
                .stream()
                .map(x -> x.getId().getEvaluatorUserId())
                .collect(Collectors.toList()))
                .stream()
                .map(UserEntityIdsView::getUsername)
                .collect(Collectors.toList());
        return new LazyContentPage<>(evaluators, evals.isLast());
    }
}
