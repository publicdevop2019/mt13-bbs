package com.hw.aggregate.post.command;

import lombok.Data;

@Data
public class DeletePostAdminCommand {
    private String postId;

    public DeletePostAdminCommand(String postId) {
        this.postId = postId;
    }
}
