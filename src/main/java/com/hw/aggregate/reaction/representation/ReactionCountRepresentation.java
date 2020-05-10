package com.hw.aggregate.reaction.representation;

import lombok.Data;

@Data
public class ReactionCountRepresentation {
    private Long count;

    public ReactionCountRepresentation(Long count) {
        this.count = count;
    }
}
