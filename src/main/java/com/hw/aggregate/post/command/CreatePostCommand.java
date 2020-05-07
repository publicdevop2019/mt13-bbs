package com.hw.aggregate.post.command;

import lombok.Data;

@Data
public class CreatePostCommand {
    private String title;
    private String topic;
    private String content;
}
