package com.hw.aggregate.reaction;

import com.hw.aggregate.comment.CommentApplicationService;
import com.hw.aggregate.post.PostApplicationService;
import com.hw.aggregate.reaction.model.*;
import com.hw.aggregate.reaction.representation.RankedUserReactionRepresentation;
import com.hw.aggregate.reaction.representation.ReactionCountRepresentation;
import com.hw.shared.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReactionApplicationService {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private IdGenerator idGenerator;
    private Map<ReferenceEnum, ReferenceService> refServices;

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
        UserReaction.createOrUpdate(cmd.getRefId(), cmd.getReferenceEnum(), cmd.getReactionEnum(), refServices, reactionRepository, cmd.getUserId(), entityManager, idGenerator);
    }

    @Transactional
    public void removeReaction(CommonReaction cmd) {
        UserReaction.delete(cmd.getUserId(), cmd.getRefId(), cmd.getReferenceEnum(), cmd.getReactionEnum(), reactionRepository);
    }

    @Transactional
    public void purgeReactions(String refId) {
        reactionRepository.purgeReactionForReference(refId);
    }

    @Transactional(readOnly = true)
    public RankedUserReactionRepresentation getLikes(Integer pageNumber, Integer pageSize, SortOrderEnum sortOrder) {
        return UserReaction.findType(ReactionEnum.LIKE, pageNumber, pageSize, sortOrder, entityManager);
    }

    @Transactional(readOnly = true)
    public RankedUserReactionRepresentation getDislikes(Integer pageNumber, Integer pageSize, SortOrderEnum sortOrder) {
        return UserReaction.findType(ReactionEnum.DISLIKE, pageNumber, pageSize, sortOrder, entityManager);
    }

    @Transactional(readOnly = true)
    public RankedUserReactionRepresentation getReports(Integer pageNumber, Integer pageSize, SortOrderEnum sortOrder) {
        return UserReaction.findType(ReactionEnum.REPORT, pageNumber, pageSize, sortOrder, entityManager);
    }

    @Transactional(readOnly = true)
    public RankedUserReactionRepresentation getNotInterested(Integer pageNumber, Integer pageSize, SortOrderEnum sortOrder) {
        return UserReaction.findType(ReactionEnum.NOT_INTERESTED, pageNumber, pageSize, sortOrder, entityManager);
    }
}
