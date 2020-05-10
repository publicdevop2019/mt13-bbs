package com.hw.aggregate.reaction.command;

import lombok.Data;

@Data
public class RemoveDislikePostCommand {
    private String id;
    private String refId;

    public RemoveDislikePostCommand(String userId, String postId) {
        this.id = userId;
        this.refId = postId;
    }
}
