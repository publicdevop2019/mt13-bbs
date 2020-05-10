package com.hw.aggregate.reaction.model;

import lombok.Data;

@Data
public class CommonReaction {
    private ReactionEnum reactionEnum;
    private ReferenceEnum referenceEnum;
    private String id;
    private String refId;

    public CommonReaction(String userId, String commentId, ReactionEnum reactionEnum, ReferenceEnum referenceEnum) {
        this.id = userId;
        this.refId = commentId;
        this.reactionEnum = reactionEnum;
        this.referenceEnum = referenceEnum;
    }
}
