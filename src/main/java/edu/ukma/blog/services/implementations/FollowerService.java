package edu.ukma.blog.services.implementations;

import edu.ukma.blog.models.composite_id.FollowerId;
import edu.ukma.blog.models.simple_interaction.Follower;
import edu.ukma.blog.models.simple_interaction.Follower_;
import edu.ukma.blog.repositories.IFollowersRepo;
import edu.ukma.blog.repositories.IPublisherStatsRepo;
import edu.ukma.blog.repositories.graph_repos.IUserNodesRepo;
import edu.ukma.blog.services.IFollowerService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowerService implements IFollowerService {

    @PersistenceContext
    private EntityManager em;

    private final IFollowersRepo followersRepo;

    private final IPublisherStatsRepo publisherStatsRepo;

    private final IUserNodesRepo userNodesRepo;

    public FollowerService(IFollowersRepo followersRepo, IPublisherStatsRepo publisherStatsRepo, IUserNodesRepo userNodesRepo) {
        this.followersRepo = followersRepo;
        this.publisherStatsRepo = publisherStatsRepo;
        this.userNodesRepo = userNodesRepo;
    }

    @Override
    @Transactional
    public void addFollower(long publisherId, long followerId) {
        FollowerId fid = new FollowerId(publisherId, followerId);
        if (!followersRepo.existsById(fid)) {
            followersRepo.save(new Follower(fid));
            publisherStatsRepo.incFollowersCount(publisherId);

            userNodesRepo.setFollow(followerId, publisherId);
        }
    }

    @Override
    public List<Long> getFollowersBlock(long publisherId, Pageable pageable) {
        return followersRepo.findAllById_PublisherId(publisherId, pageable)
                .stream().map(x -> x.getId().getSubscriberId()).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeFollower(long publisherId, long followerId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<Follower> criteriaDelete = cb.createCriteriaDelete(Follower.class);
        Root<Follower> root = criteriaDelete.from(Follower.class);
        criteriaDelete.where(cb.equal(root.get(Follower_.id), new FollowerId(publisherId, followerId)));
        boolean hasDeleted = em.createQuery(criteriaDelete).executeUpdate() == 1;
        userNodesRepo.setUnfollow(followerId, publisherId);

        if (hasDeleted) publisherStatsRepo.decFollowersCount(publisherId);
    }
}
