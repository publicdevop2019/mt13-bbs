package com.hw.aggregate.post.model;

import com.hw.aggregate.comment.command.CreateCommentCommand;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
public class Comment implements Serializable {
    private static final long serialVersionUID = 1;
    private String id;
    private Date publishedAt;
    private String publishedBy;
    private String publisherId;
    private String content;
    private String replyTo;

    public Comment(CreateCommentCommand command, String userId) {
        id = UUID.randomUUID().toString().replace("-", "");
        publishedAt = new Date();
        publishedBy = command.getPublishedBy();
        publisherId = userId;
        content = command.getContent();
        replyTo = command.getReplyTo();
    }
}
