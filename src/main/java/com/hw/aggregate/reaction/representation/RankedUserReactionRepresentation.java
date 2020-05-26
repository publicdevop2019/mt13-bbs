package com.hw.aggregate.reaction.representation;

import com.hw.aggregate.reaction.model.RankedUserReaction;
import lombok.Data;

import java.util.List;

@Data
public class RankedUserReactionRepresentation {
    private List<RankedUserReaction> results;
    private Long total;

    public RankedUserReactionRepresentation(List<RankedUserReaction> results, Long total) {
        this.results = results;
        this.total = total;
    }
}
