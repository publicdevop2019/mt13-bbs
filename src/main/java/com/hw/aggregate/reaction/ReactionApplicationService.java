package com.hw.aggregate.reaction;

import com.hw.aggregate.comment.CommentApplicationService;
import com.hw.aggregate.post.PostApplicationService;
import com.hw.aggregate.reaction.command.*;
import com.hw.aggregate.reaction.exception.LikeReferenceNotFoundException;
import com.hw.aggregate.reaction.model.CommonReaction;
import com.hw.aggregate.reaction.model.ReactionEnum;
import com.hw.aggregate.reaction.model.UserReaction;
import com.hw.aggregate.reaction.representation.ReactionCountRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReactionApplicationService {
    private static final String COMMENT = "COMMENT";
    private static final String POST = "POST";
    @Autowired
    private ReactionRepository reactionRepository;
    @Autowired
    private PostApplicationService postApplicationService;
    @Autowired
    private CommentApplicationService commentApplicationService;

    //internal
    @Transactional
    public ReactionCountRepresentation countLikeForPost(String referencedId) {
        return new ReactionCountRepresentation(reactionRepository.countReaction(referencedId, POST, ReactionEnum.LIKE));
    }

    //internal
    @Transactional
    public ReactionCountRepresentation countLikeForComment(String referencedId) {
        return new ReactionCountRepresentation(reactionRepository.countReaction(referencedId, COMMENT, ReactionEnum.LIKE));
    }

    //internal
    @Transactional
    public ReactionCountRepresentation countDislikeForPost(String referencedId) {
        return new ReactionCountRepresentation(reactionRepository.countReaction(referencedId, POST, ReactionEnum.DISLIKE));
    }

    //internal
    @Transactional
    public ReactionCountRepresentation countDislikeForComment(String referencedId) {
        return new ReactionCountRepresentation(reactionRepository.countReaction(referencedId, COMMENT, ReactionEnum.DISLIKE));
    }

    @Transactional
    private void addReaction(String userId, String referencedId, String type, ReactionEnum reactionEnum) {
        UserReaction like = UserReaction.create(referencedId, type, reactionEnum);
        reactionRepository.save(like);
    }

    @Transactional
    public void addDislikePost(AddDislikePostCommand cmd) {
        if (!postApplicationService.existById(cmd.getRefId()))
            throw new LikeReferenceNotFoundException();
        addReaction(cmd.getId(), cmd.getRefId(), POST, ReactionEnum.DISLIKE);
    }

    @Transactional
    public void addDislikeComment(AddDislikeCommentCommand cmd) {
        if (!commentApplicationService.existById(cmd.getRefId()))
            throw new LikeReferenceNotFoundException();
        addReaction(cmd.getId(), cmd.getRefId(), COMMENT, ReactionEnum.DISLIKE);
    }

    @Transactional
    public void removeDislikePost(RemoveDislikePostCommand cmd) {
        reactionRepository.deleteReaction(cmd.getId(), cmd.getId(), POST, ReactionEnum.DISLIKE);
    }

    @Transactional
    public void removeDislikeComment(RemoveDislikeCommentCommand cmd) {
        reactionRepository.deleteReaction(cmd.getId(), cmd.getId(), COMMENT, ReactionEnum.DISLIKE);
    }

    @Transactional
    public void addLikePost(AddLikePostCommand cmd) {
        if (!postApplicationService.existById(cmd.getRefId()))
            throw new LikeReferenceNotFoundException();
        addReaction(cmd.getId(), cmd.getRefId(), POST, ReactionEnum.LIKE);
    }

    @Transactional
    public void addLikeComment(AddLikeCommentCommand cmd) {
        if (!commentApplicationService.existById(cmd.getRefId()))
            throw new LikeReferenceNotFoundException();
        addReaction(cmd.getId(), cmd.getRefId(), COMMENT, ReactionEnum.LIKE);
    }

    @Transactional
    public void removePostLike(RemoveLikePostCommand cmd) {
        reactionRepository.deleteReaction(cmd.getId(), cmd.getId(), POST, ReactionEnum.LIKE);
    }

    @Transactional
    public void removeCommentLike(RemoveLikeCommentCommand cmd) {
        reactionRepository.deleteReaction(cmd.getId(), cmd.getId(), COMMENT, ReactionEnum.LIKE);
    }
}
