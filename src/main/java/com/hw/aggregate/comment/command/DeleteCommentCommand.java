package com.hw.aggregate.comment.command;

import lombok.Data;

@Data
public class DeleteCommentCommand {
    private String userId;
    private String commentId;

    public DeleteCommentCommand(String userId, String commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }
}
