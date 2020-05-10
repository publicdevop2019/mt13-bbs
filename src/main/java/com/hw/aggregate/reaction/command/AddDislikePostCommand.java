package com.hw.aggregate.reaction.command;

import com.hw.aggregate.reaction.model.CommonReaction;
import com.hw.aggregate.reaction.model.ReactionEnum;
import com.hw.aggregate.reaction.model.ReferenceEnum;
import lombok.Data;

@Data
public class AddDislikePostCommand extends CommonReaction {

    public AddDislikePostCommand(String userId, String postId) {
        super(userId, postId, ReactionEnum.DISLIKE, ReferenceEnum.POST);
    }
}
