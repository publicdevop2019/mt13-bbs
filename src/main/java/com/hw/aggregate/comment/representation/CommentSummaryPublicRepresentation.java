package com.hw.aggregate.comment.representation;

import com.hw.aggregate.comment.model.Comment;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CommentSummaryPublicRepresentation {
    private List<CommentPublicCard> commentList;

    public CommentSummaryPublicRepresentation(List<Comment> postList) {
        this.commentList = postList.stream().map(CommentPublicCard::new).collect(Collectors.toList());
    }

    @Data
    private class CommentPublicCard {
        private Long id;
        private String content;
        private String replyTo;
        private Date publishedAt;
        private String publishedBy;

        public CommentPublicCard(Comment comment) {
            this.id = comment.getId();
            this.content = comment.getContent();
            this.replyTo = comment.getReplyTo();
            this.publishedAt = comment.getCreatedAt();
            this.publishedBy = comment.getCreatedBy();
        }
    }
}
