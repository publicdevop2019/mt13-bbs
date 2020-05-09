package com.hw.aggregate.comment.representation;

import lombok.Data;

@Data
public class CommentCountPublicRepresentation {
    private Long count;

    public CommentCountPublicRepresentation(Long count) {
        this.count = count;
    }
}
