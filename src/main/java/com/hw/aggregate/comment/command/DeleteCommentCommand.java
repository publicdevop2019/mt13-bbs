package com.hw.aggregate.comment.command;

import lombok.Data;

@Data
public class DeleteCommentCommand {
    private String userId;
    private String postId;
    private String commentId;

    public DeleteCommentCommand(String userId, String postId, String commentId) {
        this.userId = userId;
        this.postId = postId;
        this.commentId = commentId;
    }
}
