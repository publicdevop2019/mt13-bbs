package com.hw.aggregate.reaction.command;

import lombok.Data;

@Data
public class AddLikeCommentCommand {
    private String id;
    private String refId;

    public AddLikeCommentCommand(String userId, String commentId) {
        this.id = userId;
        this.refId = commentId;
    }
}
