package com.hw.aggregate.reaction.command;

import lombok.Data;

@Data
public class AddDislikePostCommand {
    private String id;
    private String refId;

    public AddDislikePostCommand(String userId, String postId) {
        this.id = userId;
        this.refId = postId;
    }
}
