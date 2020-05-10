package com.hw.aggregate.reaction.model;

import com.hw.aggregate.reaction.command.AddDislikeCommentCommand;
import lombok.Data;

@Data
public class CommonReaction {
    private String id;
    private String refId;

    public CommonReaction(String userId, String commentId) {
        this.id = userId;
        this.refId = commentId;
    }
}
