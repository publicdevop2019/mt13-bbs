package com.hw.aggregate.comment.representation;

import com.hw.aggregate.comment.model.Comment;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CommentSummaryPrivateRepresentation {
    private List<CommentPrivateCard> commentList;

    public CommentSummaryPrivateRepresentation(List<Comment> postList) {
        this.commentList = postList.stream().map(CommentPrivateCard::new).collect(Collectors.toList());
    }

    @Data
    private class CommentPrivateCard {
        private Long id;
        private String content;
        private String replyTo;
        private Date publishedAt;

        public CommentPrivateCard(Comment comment) {
            this.id = comment.getId();
            this.content = comment.getContent();
            this.replyTo = comment.getReplyTo();
            this.publishedAt = comment.getCreatedAt();
        }
    }
}
