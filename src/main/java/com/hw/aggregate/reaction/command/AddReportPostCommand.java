package com.hw.aggregate.reaction.command;

import com.hw.aggregate.reaction.model.CommonReaction;
import com.hw.aggregate.reaction.model.ReactionEnum;
import com.hw.aggregate.reaction.model.ReferenceEnum;

public class AddReportPostCommand extends CommonReaction {
    public AddReportPostCommand(String userId, String refId) {
        super(userId, refId, ReactionEnum.REPORT, ReferenceEnum.POST);
    }
}
