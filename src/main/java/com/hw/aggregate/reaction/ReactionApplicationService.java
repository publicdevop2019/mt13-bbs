package com.hw.aggregate.reaction;

import com.hw.aggregate.comment.CommentApplicationService;
import com.hw.aggregate.post.PostApplicationService;
import com.hw.aggregate.reaction.exception.UnknownReactionTypeException;
import com.hw.aggregate.reaction.model.*;
import com.hw.aggregate.reaction.representation.ReactionCountRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReactionApplicationService {

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
    public void addAction(CommonReaction cmd) {
        UserReaction reaction = UserReaction.create(cmd.getRefId(), cmd.getReferenceEnum(), cmd.getReactionEnum(), refServices);
        if (cmd.getReactionEnum().equals(ReactionEnum.LIKE)) {
            reactionRepository.deleteReaction(cmd.getId(), cmd.getRefId(), cmd.getReferenceEnum(), ReactionEnum.DISLIKE);
        } else if (cmd.getReactionEnum().equals(ReactionEnum.DISLIKE)) {
            reactionRepository.deleteReaction(cmd.getId(), cmd.getRefId(), cmd.getReferenceEnum(), ReactionEnum.LIKE);
        } else {
            throw new UnknownReactionTypeException();
        }
        reactionRepository.save(reaction);
    }

    @Transactional
    public void removeAction(CommonReaction cmd) {
        reactionRepository.deleteReaction(cmd.getId(), cmd.getRefId(), cmd.getReferenceEnum(), cmd.getReactionEnum());
    }
}
