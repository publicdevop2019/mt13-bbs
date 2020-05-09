package com.hw.aggregate.post.representation;

import com.hw.aggregate.post.model.Post;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostCardSummaryRepresentation {
    private List<PostCard> postCardList;

    @Data
    public static class PostCard {
        private Long id;
        private String title;
        private String topic;
        private Date publishedAt;
        private String publisherId;
        private Long views;
        private Long comments;

        public PostCard(Post post, Long comments) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.topic = post.getTopic();
            this.publishedAt = post.getCreatedAt();
            this.publisherId = post.getCreatedBy();
            this.views = post.getViewNum();
            this.comments = comments;
        }
    }
}
