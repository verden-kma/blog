package edu.ukma.blog.services.implementations;

import edu.ukma.blog.models.Evaluation;
import edu.ukma.blog.models.compositeIDs.EvaluatorId;
import edu.ukma.blog.models.compositeIDs.RecordId;
import edu.ukma.blog.repositories.IEvaluatorsRepo;
import edu.ukma.blog.repositories.IUsersRepo;
import edu.ukma.blog.repositories.projections.UserEntityIdsView;
import edu.ukma.blog.services.IRecordEvalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecordEvalService implements IRecordEvalService {

    @Autowired
    private IEvaluatorsRepo evaluatorsRepo;

    @Autowired
    private IUsersRepo usersRepo;

    @Override
    public Boolean getReaction(RecordId recordId, long userId) {
        Optional<Evaluation> maybeEvaluator = evaluatorsRepo.findById(new EvaluatorId(recordId, userId));
        return maybeEvaluator.map(Evaluation::getIsLiker).orElse(null);
    }

    @Override
    public void putLike(RecordId recordId, long userId) {
        evaluatorsRepo.save(new Evaluation(new EvaluatorId(recordId, userId), true));
    }

    @Override
    public void removeLike(RecordId recordId, long userId) {
        evaluatorsRepo.deleteById(new EvaluatorId(recordId, userId));
    }

    @Override
    public List<String> getLikers(RecordId recordId) {
        return getEvalKind(recordId, true);
    }

    @Override
    public void putDislike(RecordId recordId, long userId) {
        evaluatorsRepo.save(new Evaluation(new EvaluatorId(recordId, userId), true));
    }

    @Override
    public void removeDislike(RecordId recordId, long userId) {
        evaluatorsRepo.deleteById(new EvaluatorId(recordId, userId));
    }

    @Override
    public List<String> getDislikers(RecordId recordId) {
        return getEvalKind(recordId, false);
    }

    private List<String> getEvalKind(RecordId recordId, boolean isLiker) {
        List<Evaluation> evals = evaluatorsRepo.findAllById_RecordIdAndIsLiker(recordId, isLiker);
        return usersRepo.getUsernamesByIds(evals
                .stream()
                .map(x -> x.getId().getEvaluatorId())
                .collect(Collectors.toList()))
                .stream()
                .map(UserEntityIdsView::getUsername)
                .collect(Collectors.toList());
    }
}
