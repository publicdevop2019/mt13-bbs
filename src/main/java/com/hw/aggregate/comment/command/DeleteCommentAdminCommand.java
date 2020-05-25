package com.hw.aggregate.comment.command;

import lombok.Data;

@Data
public class DeleteCommentAdminCommand {
    private String commentId;

    public DeleteCommentAdminCommand(String commentId) {
        this.commentId = commentId;
    }
}
