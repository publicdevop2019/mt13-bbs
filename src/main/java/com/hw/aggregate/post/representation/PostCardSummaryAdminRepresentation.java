package com.hw.aggregate.post.representation;

import lombok.Data;

import java.util.List;

@Data
public class PostCardSummaryAdminRepresentation {

    public PostCardSummaryAdminRepresentation(List<PostCardSummaryRepresentation.PostCard> results, Long total) {
        this.results = results;
        this.total = total;
    }

    private List<PostCardSummaryRepresentation.PostCard> results;
    private Long total;

}
