package com.hw.aggregate.reaction;

import com.hw.aggregate.comment.CommentApplicationService;
import com.hw.aggregate.post.PostApplicationService;
import com.hw.aggregate.reaction.exception.LikeReferenceNotFoundException;
import com.hw.aggregate.reaction.exception.UnknownReactionTypeException;
import com.hw.aggregate.reaction.exception.UnknownReferenceTypeException;
import com.hw.aggregate.reaction.model.CommonReaction;
import com.hw.aggregate.reaction.model.ReactionEnum;
import com.hw.aggregate.reaction.model.ReferenceEnum;
import com.hw.aggregate.reaction.model.UserReaction;
import com.hw.aggregate.reaction.representation.ReactionCountRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReactionApplicationService {
    @Autowired
    private ReactionRepository reactionRepository;
    @Autowired
    private PostApplicationService postApplicationService;
    @Autowired
    private CommentApplicationService commentApplicationService;

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
    public void addActionToRef(CommonReaction cmd) {
        if (cmd.getReferenceEnum().equals(ReferenceEnum.POST)) {
            if (!postApplicationService.existById(cmd.getRefId()))
                throw new LikeReferenceNotFoundException();
        } else if (cmd.getReferenceEnum().equals(ReferenceEnum.COMMENT)) {
            if (!commentApplicationService.existById(cmd.getRefId()))
                throw new LikeReferenceNotFoundException();
        } else {
            throw new UnknownReferenceTypeException();
        }
        UserReaction reaction = UserReaction.create(cmd.getRefId(), cmd.getReferenceEnum(), cmd.getReactionEnum());
        reactionRepository.save(reaction);
        if (cmd.getReactionEnum().equals(ReactionEnum.LIKE)) {
            reactionRepository.deleteReaction(cmd.getId(), cmd.getRefId(), cmd.getReferenceEnum(), ReactionEnum.DISLIKE);
        } else if (cmd.getReactionEnum().equals(ReactionEnum.DISLIKE)) {
            reactionRepository.deleteReaction(cmd.getId(), cmd.getRefId(), cmd.getReferenceEnum(), ReactionEnum.LIKE);
        } else {
            throw new UnknownReactionTypeException();
        }
    }

    @Transactional
    public void removeActionRef(CommonReaction cmd) {
        reactionRepository.deleteReaction(cmd.getId(), cmd.getRefId(), cmd.getReferenceEnum(), cmd.getReactionEnum());
    }
}
