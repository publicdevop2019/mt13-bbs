package com.hw.aggregate.like.representation;

import lombok.Data;

@Data
public class LikeRepresentation {
    private Long count;

    public LikeRepresentation(Long count) {
        this.count = count;
    }
}
