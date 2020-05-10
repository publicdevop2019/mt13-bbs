package com.hw.aggregate.reaction.command;

import lombok.Data;

@Data
public class RemoveLikePostCommand {
    private String id;
    private String refId;

    public RemoveLikePostCommand(String userId, String postId) {
        this.id = userId;
        this.refId = postId;
    }
}
