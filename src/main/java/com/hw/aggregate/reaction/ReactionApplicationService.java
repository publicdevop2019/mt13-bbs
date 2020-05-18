package com.hw.aggregate.reaction;

import com.hw.aggregate.comment.CommentApplicationService;
import com.hw.aggregate.post.PostApplicationService;
import com.hw.aggregate.reaction.model.*;
import com.hw.aggregate.reaction.representation.ReactionCountRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReactionApplicationService {

    private Map<ReferenceEnum, ReferenceService> refServices;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    public void setRefServices(PostApplicationService postApplicationService,
                               CommentApplicationService commentApplicationService) {
        HashMap<ReferenceEnum, ReferenceService> referenceEnumReferenceServiceHashMap = new HashMap<>();
        referenceEnumReferenceServiceHashMap.put(ReferenceEnum.POST, postApplicationService);
        referenceEnumReferenceServiceHashMap.put(ReferenceEnum.COMMENT, commentApplicationService);
        this.refServices = referenceEnumReferenceServiceHashMap;
    }

    @Autowired
    private ReactionRepository reactionRepository;

    //internal
    @Transactional
    public ReactionCountRepresentation countLikeForPost(String referencedId) {
        return new ReactionCountRepresentation(reactionRepository.countReaction(referencedId, ReferenceEnum.POST, ReactionEnum.LIKE));
    }

    //internal
    @Transactional
    public ReactionCountRepresentation countLikeForComment(String referencedId) {
        return new ReactionCountRepresentation(reactionRepository.countReaction(referencedId, ReferenceEnum.COMMENT, ReactionEnum.LIKE));
    }

    //internal
    @Transactional
    public ReactionCountRepresentation countDislikeForPost(String referencedId) {
        return new ReactionCountRepresentation(reactionRepository.countReaction(referencedId, ReferenceEnum.POST, ReactionEnum.DISLIKE));
    }

    //internal
    @Transactional
    public ReactionCountRepresentation countDislikeForComment(String referencedId) {
        return new ReactionCountRepresentation(reactionRepository.countReaction(referencedId, ReferenceEnum.COMMENT, ReactionEnum.DISLIKE));
    }

    @Transactional
    public void addReaction(CommonReaction cmd) {
        UserReaction.create(cmd.getRefId(), cmd.getReferenceEnum(), cmd.getReactionEnum(), refServices, reactionRepository, cmd.getUserId(), transactionManager, entityManager);
    }

    @Transactional
    public void removeReaction(CommonReaction cmd) {
        UserReaction.delete(cmd.getUserId(), cmd.getRefId(), cmd.getReferenceEnum(), cmd.getReactionEnum(), reactionRepository);
    }

    @Transactional
    public void purgeReactions(String refId) {
        reactionRepository.purgeReactionForReference(refId);
    }
}
