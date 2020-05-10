package com.hw.aggregate.reaction.command;

import lombok.Data;

@Data
public class AddLikePostCommand {
    private String id;
    private String refId;

    public AddLikePostCommand(String userId, String postId) {
        this.id = userId;
        this.refId = postId;
    }
}
