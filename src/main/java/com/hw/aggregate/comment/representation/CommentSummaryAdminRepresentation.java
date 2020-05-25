package com.hw.aggregate.comment.representation;

import com.hw.aggregate.comment.model.Comment;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CommentSummaryAdminRepresentation {
    private List<CommentAdminCard> commentList;

    public CommentSummaryAdminRepresentation(List<Comment> postList) {
        this.commentList = postList.stream().map(CommentAdminCard::new).collect(Collectors.toList());
    }

    @Data
    private class CommentAdminCard {
        private Long id;
        private String content;
        private Date publishedAt;
        private String publisherId;

        public CommentAdminCard(Comment comment) {
            this.id = comment.getId();
            this.content = comment.getContent();
            this.publishedAt = comment.getCreatedAt();
            this.publisherId = comment.getCreatedBy();
        }
    }
}
