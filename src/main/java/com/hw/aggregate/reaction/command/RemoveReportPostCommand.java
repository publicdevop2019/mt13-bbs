package com.hw.aggregate.reaction.command;

import com.hw.aggregate.reaction.model.CommonReaction;
import com.hw.aggregate.reaction.model.ReactionEnum;
import com.hw.aggregate.reaction.model.ReferenceEnum;

public class RemoveReportPostCommand extends CommonReaction {
    public RemoveReportPostCommand(String userId, String refId) {
        super(userId, refId, ReactionEnum.REPORT, ReferenceEnum.POST);
    }
}
