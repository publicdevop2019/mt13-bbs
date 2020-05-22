package com.hw.aggregate.reaction.command;

import com.hw.aggregate.reaction.model.CommonReaction;
import com.hw.aggregate.reaction.model.ReactionEnum;
import com.hw.aggregate.reaction.model.ReferenceEnum;

public class RemoveNotInterestedPostCommand extends CommonReaction {
    public RemoveNotInterestedPostCommand(String userId, String refId) {
        super(userId, refId, ReactionEnum.NOT_INTERESTED, ReferenceEnum.POST);
    }
}
