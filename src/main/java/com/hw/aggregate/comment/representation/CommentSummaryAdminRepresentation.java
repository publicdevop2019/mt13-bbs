package com.hw.aggregate.comment.representation;

import com.hw.aggregate.comment.model.Comment;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentSummaryAdminRepresentation {
    private List<CommentAdminCard> results;
    private Long total;

    public CommentSummaryAdminRepresentation(List<CommentAdminCard> results, Long total) {
        this.results = results;
        this.total = total;
    }

    @Data
    public static class CommentAdminCard {
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
