package com.hw.aggregate.reaction.command;

import lombok.Data;

@Data
public class RemoveDislikeCommentCommand {
    private String id;
    private String refId;

    public RemoveDislikeCommentCommand(String userId, String commentId) {
        this.id = userId;
        this.refId = commentId;
    }
}
