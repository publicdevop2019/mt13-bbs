package com.hw.aggregate.post.representation;

import com.hw.aggregate.post.model.Post;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostCardSummaryRepresentation {
    private List<PostCard> postCardList;

    public PostCardSummaryRepresentation(List<Post> postList) {
        this.postCardList = postList.stream().map(PostCard::new).collect(Collectors.toList());
    }

    @Data
    private class PostCard {
        private Long id;
        private String title;
        private String topic;
        private Date publishedAt;
        private String publisherId;

        public PostCard(Post post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.topic = post.getTopic();
            this.publishedAt = post.getCreatedAt();
            this.publisherId = post.getCreatedBy();
        }
    }
}
