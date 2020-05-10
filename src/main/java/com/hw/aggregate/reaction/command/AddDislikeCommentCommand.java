package com.hw.aggregate.reaction.command;

import lombok.Data;

@Data
public class AddDislikeCommentCommand {
    private String id;
    private String refId;

    public AddDislikeCommentCommand(String userId, String postId) {
        this.id = userId;
        this.refId = postId;
    }
}
