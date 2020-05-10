package com.hw.aggregate.reaction.command;

import lombok.Data;

@Data
public class RemoveLikeCommentCommand {
    private String id;
    private String refId;

    public RemoveLikeCommentCommand(String userId, String commentId) {
        this.id = userId;
        this.refId = commentId;
    }
}
