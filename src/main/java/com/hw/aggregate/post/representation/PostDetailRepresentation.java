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
    private Long likeNum;
    private Boolean userModified;
    private Long dislikeNum;

    public PostDetailRepresentation(Post post, Long likeNum, Long dislikeNum) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.topic = post.getTopic();
        this.publishedAt = post.getCreatedAt();
        this.publishedBy = post.getCreatedBy();
        this.userModified = post.getUserModified();
        this.content = post.getContent();
        this.likeNum = likeNum;
        this.dislikeNum = dislikeNum;
    }
}
