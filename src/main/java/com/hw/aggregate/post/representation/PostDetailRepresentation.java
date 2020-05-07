package com.hw.aggregate.post.representation;

import com.hw.aggregate.post.model.Comment;
import com.hw.aggregate.post.model.Post;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostDetailRepresentation {
    private Long id;
    private String title;
    private String topic;
    private Date publishedAt;
    private String publisherId;
    private String content;
    private List<Comment> commentList;

    public PostDetailRepresentation(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.topic = post.getTopic();
        this.publishedAt = post.getPublishAt();
        this.publisherId = post.getCreatedBy();
        this.content = post.getContent();
        this.commentList = post.getCommentList();
    }
}
