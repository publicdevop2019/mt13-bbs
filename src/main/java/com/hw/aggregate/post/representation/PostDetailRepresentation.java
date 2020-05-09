package com.hw.aggregate.post.representation;

import com.hw.aggregate.post.model.Post;
import lombok.Data;

import java.util.Date;

@Data
public class PostDetailRepresentation {
    private Long id;
    private String title;
    private String topic;
    private Date publishedAt;
    private String publishedBy;
    private String content;

    public PostDetailRepresentation(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.topic = post.getTopic();
        this.publishedAt = post.getCreatedAt();
        this.publishedBy = post.getCreatedBy();
        this.content = post.getContent();
    }
}
