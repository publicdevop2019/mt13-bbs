package com.hw.aggregate.comment.command;

import lombok.Data;

@Data
public class CreateCommentCommand {
    private String content;
    private String replyTo;
}
