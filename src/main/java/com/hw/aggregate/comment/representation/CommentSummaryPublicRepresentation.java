package com.hw.aggregate.comment.representation;

import com.hw.aggregate.comment.model.Comment;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentSummaryPublicRepresentation {
    public CommentSummaryPublicRepresentation(List<CommentPublicCard> commentList) {
        this.commentList = commentList;
    }

    private List<CommentPublicCard> commentList;

    @Data
    public static class CommentPublicCard {
        private Long id;
        private String content;
        private String replyTo;
        private Date publishedAt;
        private String publishedBy;
        private Long likeNum;
        private Long dislikeNum;

        public CommentPublicCard(Comment comment, Long likeNum, Long dislikeNum) {
            this.id = comment.getId();
            this.content = comment.getContent();
            this.replyTo = comment.getReplyTo();
            this.publishedAt = comment.getCreatedAt();
            this.publishedBy = comment.getCreatedBy();
            this.likeNum = likeNum;
            this.dislikeNum = dislikeNum;
        }
    }
}
