package com.hw.aggregate.post.command;

import lombok.Data;

@Data
public class DeletePostCommand {
    private String userId;
    private String postId;

    public DeletePostCommand(String userId, String postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
